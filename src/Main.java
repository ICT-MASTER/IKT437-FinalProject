import javax.swing.*;

public class Main {

    public static void main(String[] args) {


        try {
            // Set cross-platform Java L&F (also called "Metal")
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException e) {
            // handle exception
        }
        catch (ClassNotFoundException e) {
            // handle exception
        }
        catch (InstantiationException e) {
            // handle exception
        }
        catch (IllegalAccessException e) {
            // handle exception
        }

        UserInterface ui = new UserInterface();
        ui.setSize(450,500);
        ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



    }
}
