package filemanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.awt.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

public class IOManager {
    private static Path addressPath = Paths.get("C:\\");
    private static ObservableList<Path> observablePathList;

    //initialization of ObservableList -> needed for ListView
    static {
        try {
            getObservablePathList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Listing all dirs and files in actual address path.
    public static List<Path> getPathList() throws IOException {
        List<Path> list = Files.list(IOManager.getAddressPath()).collect(Collectors.toList());
        for (ListIterator<Path> iter = list.listIterator(); iter.hasNext(); ) {
            Path element = iter.next();
            iter.set(element.getFileName());
        }
        return list;
    }

    //Listing all dirs and files in actual address path.
    //Converting type of STREAM (Files.list) to List<Path> and then to ObservableList
    public static ObservableList<Path> getObservablePathList() throws IOException {
        observablePathList = FXCollections.observableList(getPathList());
        return observablePathList;
    }

    //Setting static variable addressPath a new value of actual address sent from address field (FileManagerController)
    //check if value from address field is shorter then 3 chars (i.e. C: or C)
    //check if such address exists, if yes then set the value to addressPath
    public static boolean setPath(String path) throws IOException {
        Path address = Paths.get(path);
        if (address.toString().length() < 3)
            return false;
        if (Files.exists(address)) {
            addressPath = Paths.get(address.toString());
            return true;
        }
        return false;
    }

    //Setting static variable addressPath a new value of actual address sent from address field (FileManagerController)
    //Setting DirectoryType as DIRECTORY -> it always ends as dir *
    //* even if it is a file (after opening, addresspath is set to parent() so to the directory)
    //check if value from address field is shorter then 3 chars (i.e. C: or C)
    //check if such address exists, if yes then set the value to addressPath
    //if the address can be run, then do it
    //if the address is a directory, then just return true (the address will be used to show dirs and files there)
    public static boolean setPathOrOpen(String path) throws IOException {
        Path address = Paths.get(path);
        if (address.toString().length() < 3)
            return false;
        if (Files.exists(address)) {
            addressPath = Paths.get(address.toString());
            if (!Files.isDirectory(addressPath)) {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(addressPath.toFile());
                moveBackPath();
                return true;
            } else if (Files.isDirectory(addressPath))
                return true;
        }
        return false;
    }

    //getter of addressPath static var
    public static Path getAddressPath() {
        return addressPath;
    }

    //returns extension of file or returns empty String for directories saved in addressPath
    //checks if the fileName contains dot if yes then finds last appearance and cuts whole file name from there
    //if does not contain a dot then returns empty String
    public static String getExtension() {
        Path fileName = addressPath.getFileName();
        if (fileName.toString().contains(".")) {
            int dotIndex = fileName.toString().lastIndexOf(".");
            return fileName.toString().substring(dotIndex);
        }
        return "";
    }

    //returns extension of file or returns empty String for directories sent by method argument
    //checks if the fileName contains dot if yes then finds last appearance and cuts whole file name from there
    //if does not contain a dot then returns empty String
    public static String getExtension(Path path) {
        path = path.getFileName();
        if (path.toString().contains(".")) {
            int dotIndex = path.toString().lastIndexOf(".");
            return path.toString().substring(dotIndex);
        }
        return "";
    }

    //Cuts file extension (if present) and returns pure file name
    public static String getFileNameOnly() {
        Path fileName = addressPath.getFileName();
        if (fileName.toString().contains(".")) {
            int dotIntex = fileName.toString().lastIndexOf(".");
            return fileName.toString().substring(0, dotIntex);
        }
        return addressPath.getFileName().toString();
    }

    //go to the upper address if possible
    //if the address field has less than 3 chars or is a root (C:\) then return false
    public static boolean moveBackPath() throws IOException {
        if (!(addressPath.toString().length() <= 3)) {
            setPathOrOpen(addressPath.getParent().toString());
            return true;
        }
        return false;
    }

    //Creates a new file
    //Check if the files exists, if not then create it
    //set addressPath back to the path without file name
    //try block catches AccessDeniedException
    //returns: 1 - correctly edited | 0 - name already exists | -1 - access denied
    public static int createNewFile(String fileName) throws IOException {
        addressPath = Paths.get(addressPath.toString() + "\\" + fileName);
        if (!Files.exists(addressPath)) {
            try {
                Files.createFile(addressPath);
                return 1;
            } catch (AccessDeniedException ade) {
                return -1;
            } finally {
                moveBackPath();
            }
        }
        moveBackPath();
        return 0;
    }

    //Creates a new folder
    //Check if the folder exists, if not then create it
    //set addressPath back to the path without folder name
    //try block catches AccessDeniedException
    //returns: 1 - correctly edited | 0 - name already exists | -1 - access denied
    public static int createNewFolder(String fileName) throws IOException {
        addressPath = Paths.get(addressPath.toString() + "\\" + fileName);
        if (!Files.exists(addressPath)) {
            try {
                Files.createDirectory(addressPath);
                return 1;
            } catch (AccessDeniedException ade) {
                return -1;
            } finally {
                moveBackPath();
            }
        }
        moveBackPath();
        return 0;
    }


    //Editing name of file or directory
    //Setting newNamePath -> parent dir of addresspath + new name file + extension
    //if such a file does not exist then it renames
    //at the end addresspath is set back to the parent dir
    //try block catches AccessDeniedException
    //returns: 1 - correctly edited | 0 - name already exists | -1 - access denied
    public static int editName(String newName) throws IOException {
        String newNamePath = addressPath.getParent() + "\\" + newName + getExtension();
        if (!Files.exists(Paths.get(newNamePath))) {
            try {
                Files.move(addressPath, addressPath.resolveSibling(newNamePath));
                return 1;
            } catch (AccessDeniedException ade) {
                return -1;
            } finally {
                moveBackPath();
            }
        }
        moveBackPath();
        return 0;
    }


    //TODO DIRECTORYNOTEMPTYEXCEPTION HANDLER
    //deletes sent path if exists
    //returns: 1 - correctly deleted | 0 - not deleted | -1 - access denied | -2 - not empty dir
    public static int deletePath(String path) throws IOException {
        setPath(path);
        try {
            if (Files.deleteIfExists(Paths.get(path))) return 1;
            else return 0;
        } catch (AccessDeniedException | DirectoryNotEmptyException ex) {
            if (ex instanceof AccessDeniedException)
                return -1;
            else return -2;
        } finally {
            moveBackPath();
        }

    }

    // Creates String with regex (it will look for sent string and any characters between it)
    // Creates ObservableList that will be returned to finding methods in FileManagerController
    //for loop is using getPathList() method to get actual list of paths in addressPath var
    public static ObservableList<Path> find(String toFind) throws IOException {
        String regexToFind = "regex:.*" + toFind + ".*";
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher(regexToFind);
        ObservableList<Path> foundPathsList = FXCollections.observableList(new ArrayList<>());

        for (Path item : getPathList()) {
            if (matcher.matches(item))
                foundPathsList.add(item);
        }
        return foundPathsList;
    }

}
