import edu.rit.swen755.faultmonitor.MonitorMain;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joanna
 */
public class ServerStarter extends javax.swing.JFrame {

    private Process processJudge;
    private Process processMonitor;

    /**
     * Creates new form ServerStarter
     */
    public ServerStarter() throws InterruptedException {
        try {
            initComponents();
            processMonitor = startProcess("edu.rit.swen755.faultmonitor.MonitorMain", null);
            Thread.sleep(1000);
            processJudge = startProcess("edu.rit.swen755.judge.JudgeMain", "RUN");
        } catch (IOException ex) {
            Logger.getLogger(ServerStarter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        textAreaJudgePassive = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        textAreaMonitor = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        textAreaJudgeActive = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        textAreaJudgePassive.setColumns(20);
        textAreaJudgePassive.setRows(5);
        jScrollPane1.setViewportView(textAreaJudgePassive);

        textAreaMonitor.setColumns(20);
        textAreaMonitor.setRows(5);
        jScrollPane2.setViewportView(textAreaMonitor);

        textAreaJudgeActive.setColumns(20);
        textAreaJudgeActive.setRows(5);
        jScrollPane3.setViewportView(textAreaJudgeActive);

        jLabel1.setText("Monitor");

        jLabel2.setText("Judge - Active Instance");

        jLabel3.setText("Judge - Passive Instance");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(123, 123, 123)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 120, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(21, 21, 21)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(281, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(154, 154, 154)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(148, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private Process startProcess(String className, String args) throws IOException {
        String classpath = System.getProperty("user.dir") + "\\build\\classes";
        ProcessBuilder builder;
        if (args != null) {
            builder = new ProcessBuilder("java", "-cp", classpath, className, args);
        } else {
            builder = new ProcessBuilder("java", "-cp", classpath, className);
        }
        Process p = builder.start();

        
        ProcessReader processReaderStd = new ProcessReader(p.getInputStream());
        ProcessReader processReaderError = new ProcessReader(p.getErrorStream());
        
        Thread outReaderStd = new Thread(processReaderStd);
        outReaderStd.start();
        Thread outReaderError = new Thread(processReaderError);
        outReaderError.start();

        return p;
    }

    private class ProcessReader implements Runnable {

        private BufferedReader br;

        public ProcessReader(InputStream is) {
            InputStreamReader isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
        }

        @Override
        public void run() {

            String line = null;
            try {

                while ((line = br.readLine()) != null) {
                    System.out.println("LINE>> " + line);
                    if (line.startsWith("[MONITOR]")) {
                        textAreaMonitor.append(line.replace("[MONITOR]", "").trim() + "\n");
                        textAreaMonitor.setCaretPosition(textAreaMonitor.getDocument().getLength());
                    } else if (line.startsWith("[JUDGE-ACTIVE]")) {
                        textAreaJudgeActive.append(line.replace("[JUDGE-ACTIVE]", "").trim() + "\n");
                        textAreaJudgeActive.setCaretPosition(textAreaJudgeActive.getDocument().getLength());
                    } else if (line.startsWith("[JUDGE-PASSIVE]")) {
                        textAreaJudgePassive.append(line.replace("[JUDGE-PASSIVE]", "").trim() + "\n");
                        textAreaJudgePassive.setCaretPosition(textAreaJudgePassive.getDocument().getLength());
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(MonitorMain.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public void destroyProcess() {
        processJudge.destroy();
        processMonitor.destroy();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ServerStarter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServerStarter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServerStarter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServerStarter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    final ServerStarter starter = new ServerStarter();
                    starter.setVisible(true);
                    
                    Runtime.getRuntime().addShutdownHook(new Thread() {
                        @Override
                        public void run() {
                            starter.destroyProcess();
                        }
                    });
                } catch (InterruptedException ex) {
                    Logger.getLogger(ServerStarter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea textAreaJudgeActive;
    private javax.swing.JTextArea textAreaJudgePassive;
    private javax.swing.JTextArea textAreaMonitor;
    // End of variables declaration//GEN-END:variables

}
