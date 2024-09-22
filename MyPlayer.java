import java.io.*;
import java.util.Scanner;

class Node {
    String song;
    Node next;
    Node prev;
}

public class MyPlayer {
    static Node top, temp, top1;

    static void toFile(String a) {
        try {
            FileWriter f1 = new FileWriter("playlist.txt", true);
            f1.write(a + "\n");
            f1.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    static void addNode(Node first) {
        Scanner sc = new Scanner(System.in);
        String a;
        while (first.next != null) {
            first = first.next;
        }
        first.next = new Node();
        first.next.prev = first;
        first = first.next;
        System.out.println("\nEnter Song name- ");
        a = sc.next();
        first.song = a;
        toFile(a);
        first.next = null;
    }

    static void addNodeFromFile(Node first, String a) {
        while (first.next != null) {
            first = first.next;
        }
        first.next = new Node();
        first.next.prev = first;
        first = first.next;
        first.song = a;
        first.next = null;
    }

    static void deleteFile(String a) {
        try {
            File inputFile = new File("playlist.txt");
            File tempFile = new File("temp.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String lineToRemove = a;
            String currentLine;

            boolean found = false;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.trim().equals(lineToRemove)) {
                    found = true;
                    continue;
                }
                writer.write(currentLine + System.lineSeparator());
            }
            writer.close();
            reader.close();
            if (found) {
                inputFile.delete();
                tempFile.renameTo(inputFile);
                System.out.println("=> Song has been deleted.");
            } else {
                System.out.println("# Song not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void printList(Node first) {
        System.out.println("\nPlaylist Name- ");
        while (first.next != null) {
            System.out.println(first.song);
            first = first.next;
        }
        System.out.println(first.song);
    }

    static void countNodes(Node first) {
        int i = 0;
        while (first.next != null) {
            first = first.next;
            i++;
        }
        i++;
        System.out.println("\nTotal songs- " + (i - 1));
    }

    static Node deleteAtPos(Node pointer, int pos) {
        Node n1, prev1, temp;
        prev1 = new Node();
        temp = new Node();
        int i = 0;

        if (pos == 1) {
            temp = pointer;
            deleteFile(temp.song);
            pointer = pointer.next;
            pointer.prev = null;
            return pointer;
        }
        while (i < pos - 1) {
            prev1 = pointer;
            pointer = pointer.next;
            i++;
        }

        if (pointer.next == null) {
            temp = pointer;
            deleteFile(temp.song);

            prev1.next.prev = null;
            prev1.next = null;
        } else {
            temp = pointer;
            deleteFile(temp.song);

            prev1.next = temp.next;
            temp.next.prev = prev1;
        }
        return pointer;
    }

    static void search(Node first) {
        Scanner sc = new Scanner(System.in);
        System.out.println("\nEnter song to be searched- ");
        String song = sc.next();
        int flag = 0;

        while (first != null) {
            if (first.song.equals(song)) {
                System.out.println("\n# Song Found");
                flag++;
                break;
            } else {
                first = first.next;
            }
        }
        if (flag == 0) {
            System.out.println("\n# Song Not found");
        }
    }

    static void create() {
        top = null;
    }

    static void push(String data) {
        if (top == null) {
            top = new Node();
            top.next = null;
            top.song = data;
        } else if (!top.song.equals(data)) {
            temp = new Node();
            temp.next = top;
            temp.song = data;
            top = temp;
        }
    }

    static void display() {
        top1 = top;
        if (top1 == null) {
            System.out.println("\n=> NO recently played tracks.");
            return;
        }
        System.out.println("\n# Recently played tracks-");
        while (top1 != null) {
            System.out.println(top1.song);
            top1 = top1.next;
        }
    }

    static void play(Node first) {
        Scanner sc = new Scanner(System.in);
        printList(first);
        System.out.println("\nChoose song you wish to play- ");
        String song = sc.next();
        int flag = 0;

        while (first != null) {
            if (first.song.equals(song)) {
                System.out.println("\n=> Now Playing...... " + song);
                flag++;
                push(song);
                break;
            } else {
                first = first.next;
            }
        }
        if (flag == 0) {
            System.out.println("\n# Song Not found");
        }
    }

    static void recent() {
        display();
    }

    static void lastPlayed() {
        top1 = top;
        if (top1 == null) {
            System.out.println("\n# NO last played tracks.");
            return;
        }
        System.out.println("=> Last Played Song - " + top.song);
    }

    static void addPlaylist(Node start) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("playlist.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                addNodeFromFile(start, line);
            }
            System.out.println("=> Playlist Added");
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void deleteMenu(Node start) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Delete song?\n1.By Search\n2.By Position");
        System.out.println("Enter your choice- ");
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                deleteBySearch(start);
                break;
            case 2:
                System.out.println("\nEnter the song position: ");
                int pos = sc.nextInt();
                deleteAtPos(start, pos - 1);
                break;
        }
    }

    static void deleteBySearch(Node start) {
        Scanner sc = new Scanner(System.in);
        printList(start);
        System.out.println("\nChoose song you wish to delete- ");
        String song = sc.next();
        int flag = 0;
        while (start != null) {
            if (start.song.equals(song)) {
                System.out.println("\n# Song Found");
                Node temp = start;
                deleteFile(temp.song);
                temp.prev.next = temp.next;
                temp.next.prev = temp.prev;
                flag++;
                break;
            } else {
                start = start.next;
            }
        }
        if (flag == 0) {
            System.out.println("\n# Song Not found");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Node start = new Node();
        Node hold;
        System.out.println("\t**WELCOME**");
        System.out.println("**Please use '_' for space.");
        System.out.println("\nEnter your playlist name-  ");
        start.song = sc.nextLine();
        start.next = null;
        hold = start;
        create();

        int choice;
        do {
            System.out.println("\n1.Add New Song\n2.Delete Song\n3.Display Playlist\n4.Total Songs\n5.Search Song\n6.Play Song\n7.Recently Played\n8.Last Played\n9. Add From File\n10.Exit");
            System.out.println("\nEnter your choice- ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    addNode(start);
                    break;
                case 2:
                    deleteMenu(start);
                    break;
                case 3:
                    printList(start);
                    break;
                case 4:
                    countNodes(hold);
                    break;
                case 5:
                    search(start);
                    break;
                case 6:
                    play(start);
                    break;
                case 7:
                    recent();
                    break;
                case 8:
                    lastPlayed();
                    break;
                case 9:
                    addPlaylist(start);
                    break;
                case 10:
                    System.exit(0);
            }
        } while (choice != 11);
    }
}
