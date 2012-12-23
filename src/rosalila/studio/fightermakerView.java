/*
 * fightermakerView.java
 */

package rosalila.studio;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Frame;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.TreeSelectionEvent;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.imageio.ImageIO;


import java.io.File;
import java.io.FileOutputStream;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.util.ArrayList;
import javax.swing.JOptionPane;
/**
 * The application's main frame.
 */
public class fightermakerView extends FrameView implements TreeSelectionListener {

    Document main_doc;
    Document sfx_doc;
    Document hitboxes_doc;
    Document sprites_doc;
    String directory_path;
    
    Element current_move;
    Element current_frame;
    Element current_hitbox;
    
    ArrayList<Hitbox> red_hitboxes,blue_hitboxes;
    
    NodeList listOfMoves_main_file;
    NodeList listOfMoves_hitboxes_file;
    
    String hitbox_color_selected;
    int hitbox_index_selected;
    int frame_index_selected;    
    
    public fightermakerView(SingleFrameApplication app) {
        super(app);
        
        current_move=null;
        current_frame=null;
        current_hitbox=null;
        
        red_hitboxes=new ArrayList<Hitbox>();
        blue_hitboxes=new ArrayList<Hitbox>();
        
        listOfMoves_main_file=null;
        listOfMoves_hitboxes_file=null;
        
        hitbox_color_selected="";
        hitbox_index_selected=-1;
        frame_index_selected=-1;
        
        this.setFrame(new JFrame("Rosalila engine hitboxes editor"));
        //bad code to remove warning
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(org.jdesktop.application.SessionStorage.class.getName());
        logger.setLevel(java.util.logging.Level.OFF);

        initComponents();
        
        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {

                }
            }
        });
        
        JFrame mainFrame = fightermaker.getApplication().getMainFrame();
        mainFrame.setTitle("Buurn baby!");
    }

    private DefaultMutableTreeNode processHierarchy(Object[] hierarchy) {
    DefaultMutableTreeNode node =
      new DefaultMutableTreeNode(hierarchy[0]);
    DefaultMutableTreeNode child;
    for(int i=1; i<hierarchy.length; i++) {
      Object nodeSpecifier = hierarchy[i];
      if (nodeSpecifier instanceof Object[])  // Ie node with children
        child = processHierarchy((Object[])nodeSpecifier);
      else
        child = new DefaultMutableTreeNode(nodeSpecifier); // Ie Leaf
      node.add(child);
    }
    return(node);
  }
    
    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = fightermaker.getApplication().getMainFrame();
            aboutBox = new fightermakerAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        fightermaker.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        image_panel = new rosalila.studio.ImagePanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        list_red_hitboxes = new javax.swing.JList();
        jScrollPane10 = new javax.swing.JScrollPane();
        list_frames = new javax.swing.JList();
        jScrollPane11 = new javax.swing.JScrollPane();
        list_blue_hitboxes = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        label_current_sprite = new javax.swing.JLabel();
        spinner_x1 = new javax.swing.JSpinner();
        spinner_y1 = new javax.swing.JSpinner();
        spinner_x2 = new javax.swing.JSpinner();
        spinner_y2 = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        button_add_red_hitbox = new javax.swing.JButton();
        button_add_blue_hitbox = new javax.swing.JButton();
        button_delete_selected = new javax.swing.JButton();
        button_clean_blue_list = new javax.swing.JButton();
        button_clean_red_list = new javax.swing.JButton();
        button_remove_blue_list = new javax.swing.JButton();
        button_remove_red_list = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        list_moves = new javax.swing.JList();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();

        mainPanel.setName("panel_main"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jPanel1.setAlignmentX(0.0F);
        jPanel1.setAlignmentY(0.0F);
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(1408, 800));

        jPanel11.setName("jPanel11"); // NOI18N
        jPanel11.setPreferredSize(new java.awt.Dimension(900, 507));

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(rosalila.studio.fightermaker.class).getContext().getResourceMap(fightermakerView.class);
        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        image_panel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        image_panel.setName("image_panel"); // NOI18N
        image_panel.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                image_panelMouseWheelMoved(evt);
            }
        });
        image_panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                image_panelMouseClicked(evt);
            }
        });
        image_panel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                image_panelMouseDragged(evt);
            }
        });

        javax.swing.GroupLayout image_panelLayout = new javax.swing.GroupLayout(image_panel);
        image_panel.setLayout(image_panelLayout);
        image_panelLayout.setHorizontalGroup(
            image_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 388, Short.MAX_VALUE)
        );
        image_panelLayout.setVerticalGroup(
            image_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 431, Short.MAX_VALUE)
        );

        jScrollPane9.setName("jScrollPane9"); // NOI18N

        list_red_hitboxes.setName("list_red_hitboxes"); // NOI18N
        list_red_hitboxes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                list_red_hitboxesMousePressed(evt);
            }
        });
        jScrollPane9.setViewportView(list_red_hitboxes);

        jScrollPane10.setName("jScrollPane10"); // NOI18N

        list_frames.setName("list_frames"); // NOI18N
        list_frames.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                list_framesMousePressed(evt);
            }
        });
        jScrollPane10.setViewportView(list_frames);

        jScrollPane11.setName("jScrollPane11"); // NOI18N

        list_blue_hitboxes.setName("list_blue_hitboxes"); // NOI18N
        list_blue_hitboxes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                list_blue_hitboxesMousePressed(evt);
            }
        });
        jScrollPane11.setViewportView(list_blue_hitboxes);

        jLabel2.setFont(resourceMap.getFont("jLabel2.font")); // NOI18N
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setFont(resourceMap.getFont("jLabel3.font")); // NOI18N
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        label_current_sprite.setText(resourceMap.getString("label_current_sprite.text")); // NOI18N
        label_current_sprite.setName("label_current_sprite"); // NOI18N

        spinner_x1.setName("spinner_x1"); // NOI18N
        spinner_x1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinner_x1StateChanged(evt);
            }
        });
        spinner_x1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                spinner_x1PropertyChange(evt);
            }
        });

        spinner_y1.setName("spinner_y1"); // NOI18N
        spinner_y1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinner_y1StateChanged(evt);
            }
        });

        spinner_x2.setName("spinner_x2"); // NOI18N
        spinner_x2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinner_x2StateChanged(evt);
            }
        });

        spinner_y2.setName("spinner_y2"); // NOI18N
        spinner_y2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinner_y2StateChanged(evt);
            }
        });

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        button_add_red_hitbox.setText(resourceMap.getString("button_add_red_hitbox.text")); // NOI18N
        button_add_red_hitbox.setName("button_add_red_hitbox"); // NOI18N
        button_add_red_hitbox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button_add_red_hitboxMousePressed(evt);
            }
        });

        button_add_blue_hitbox.setText(resourceMap.getString("button_add_blue_hitbox.text")); // NOI18N
        button_add_blue_hitbox.setName("button_add_blue_hitbox"); // NOI18N
        button_add_blue_hitbox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button_add_blue_hitboxMousePressed(evt);
            }
        });

        button_delete_selected.setText(resourceMap.getString("button_delete_selected.text")); // NOI18N
        button_delete_selected.setName("button_delete_selected"); // NOI18N
        button_delete_selected.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button_delete_selectedMousePressed(evt);
            }
        });

        button_clean_blue_list.setText(resourceMap.getString("button_clean_blue_list.text")); // NOI18N
        button_clean_blue_list.setName("button_clean_blue_list"); // NOI18N
        button_clean_blue_list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button_clean_blue_listMousePressed(evt);
            }
        });

        button_clean_red_list.setText(resourceMap.getString("button_clean_red_list.text")); // NOI18N
        button_clean_red_list.setName("button_clean_red_list"); // NOI18N
        button_clean_red_list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button_clean_red_listMousePressed(evt);
            }
        });

        button_remove_blue_list.setText(resourceMap.getString("button_remove_blue_list.text")); // NOI18N
        button_remove_blue_list.setName("button_remove_blue_list"); // NOI18N
        button_remove_blue_list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button_remove_blue_listMousePressed(evt);
            }
        });

        button_remove_red_list.setText(resourceMap.getString("button_remove_red_list.text")); // NOI18N
        button_remove_red_list.setName("button_remove_red_list"); // NOI18N
        button_remove_red_list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button_remove_red_listMousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel1)
                                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE))
                            .addComponent(jLabel2)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jScrollPane11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel11Layout.createSequentialGroup()
                                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel11Layout.createSequentialGroup()
                                                .addComponent(jLabel4)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(spinner_x1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel11Layout.createSequentialGroup()
                                                .addComponent(jLabel6)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(spinner_x2, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(jPanel11Layout.createSequentialGroup()
                                                .addComponent(jLabel5)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(spinner_y1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                                                .addComponent(jLabel7)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(spinner_y2, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                        .addComponent(image_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(label_current_sprite)
                        .addContainerGap(731, Short.MAX_VALUE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(button_remove_blue_list)
                            .addComponent(button_add_blue_hitbox)
                            .addComponent(button_clean_blue_list))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(button_add_red_hitbox)
                                .addGap(65, 65, 65)
                                .addComponent(button_delete_selected))
                            .addComponent(button_clean_red_list)
                            .addComponent(button_remove_red_list))
                        .addGap(378, 378, 378))))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(spinner_x1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(spinner_y1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(image_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(1, 1, 1)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spinner_x2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(spinner_y2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button_add_blue_hitbox)
                    .addComponent(button_add_red_hitbox)
                    .addComponent(button_delete_selected))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button_clean_red_list)
                    .addComponent(button_clean_blue_list))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button_remove_blue_list)
                    .addComponent(button_remove_red_list))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(label_current_sprite)
                .addGap(21, 21, 21))
        );

        jScrollPane8.setName("jScrollPane8"); // NOI18N

        list_moves.setName("list_moves"); // NOI18N
        list_moves.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                list_movesMousePressed(evt);
            }
        });
        jScrollPane8.setViewportView(list_moves);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 858, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(274, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 609, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 776, Short.MAX_VALUE))
                .addContainerGap())
        );

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1142, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 654, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane1.getAccessibleContext().setAccessibleParent(mainPanel);

        mainPanel.getAccessibleContext().setAccessibleName(resourceMap.getString("panel_main.AccessibleContext.accessibleName")); // NOI18N
        mainPanel.getAccessibleContext().setAccessibleDescription(resourceMap.getString("panel_main.AccessibleContext.accessibleDescription")); // NOI18N

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        jMenuItem1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem1MousePressed(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItem1MouseClicked(evt);
            }
        });
        fileMenu.add(jMenuItem1);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(rosalila.studio.fightermaker.class).getContext().getActionMap(fightermakerView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setComponent(mainPanel);
        setMenuBar(menuBar);
    }// </editor-fold>//GEN-END:initComponents

private void jMenuItem1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem1MouseClicked
    //Erase me if you can
}//GEN-LAST:event_jMenuItem1MouseClicked

private void jMenuItem1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem1MousePressed
    try {

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            JFrame mainFrame = fightermaker.getApplication().getMainFrame();
            
            //Load XML file            
            JFileChooser chooser = new JFileChooser(); 
            chooser.setCurrentDirectory(new java.io.File("."));
            chooser.setDialogTitle("Choose you character");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.showOpenDialog(mainFrame);

            File selFile = chooser.getSelectedFile();
            directory_path = selFile.getPath();

            //Parse XML file            
            main_doc = docBuilder.parse (new File(directory_path+"/main.xml"));
            sfx_doc = docBuilder.parse (new File(directory_path+"/sfx.xml"));
            hitboxes_doc = docBuilder.parse (new File(directory_path+"/hitboxes.xml"));
            sprites_doc = docBuilder.parse (new File(directory_path+"/sprites.xml"));

            // normalize text representation
            main_doc.getDocumentElement ().normalize ();

            listOfMoves_main_file = main_doc.getElementsByTagName("Move");

            DefaultListModel model = new DefaultListModel();
            
            for(int s=0; s<listOfMoves_main_file.getLength() ; s++)
            {
                model.add(s, ((Element)listOfMoves_main_file.item(s)).getAttribute("name"));
            }
            
            list_moves.setModel(model);

        }catch (SAXParseException err) {
        System.out.println ("** Parsing error" + ", line " 
             + err.getLineNumber () + ", uri " + err.getSystemId ());
        System.out.println(" " + err.getMessage ());

        }catch (SAXException e) {
        Exception x = e.getException ();
        ((x == null) ? e : x).printStackTrace ();

        }catch (Throwable t) {
        t.printStackTrace ();
        }
}//GEN-LAST:event_jMenuItem1MousePressed

private void list_movesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list_movesMousePressed
    current_move=null;
    current_frame=null;
    current_hitbox=null;
    hitbox_index_selected=-1;
    frame_index_selected=-1;
    //Add frames
    listOfMoves_main_file = main_doc.getElementsByTagName("Move");

    DefaultListModel model = new DefaultListModel();

    for(int s=0; s<listOfMoves_main_file.getLength() ; s++)
    {
        Node move_node = listOfMoves_main_file.item(s);
        Element move_element = (Element)move_node;
        if(move_element.getAttribute("name").equals(list_moves.getSelectedValue()))
        {
            int frames = Integer.parseInt(move_element.getAttribute("frames"));
            for(int i=0;i<frames;i++)
            {
                model.add(i, "frame "+(i+1));
            }
            break;
        }
    }

    list_frames.setModel(model);

    //Clear hitboxes list
    DefaultListModel clean_model = new DefaultListModel();
    list_red_hitboxes.setModel(clean_model);
    list_blue_hitboxes.setModel(clean_model);
    
    //Upadate current_move
    listOfMoves_hitboxes_file = hitboxes_doc.getElementsByTagName("Move");
    for(int s=0; s<listOfMoves_hitboxes_file.getLength() ; s++)//Move loop
    {
        Node move_node = listOfMoves_hitboxes_file.item(s);
        Element move_element = (Element)move_node;
        if(move_element.getAttribute("name").equals(list_moves.getSelectedValue()))
        {
            current_move=move_element;
        }
    }
}//GEN-LAST:event_list_movesMousePressed

private void list_blue_hitboxesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list_blue_hitboxesMousePressed
    if(blue_hitboxes.size()<=list_blue_hitboxes.getSelectedIndex())
    {
        return;
    }
    
    hitbox_color_selected="blue";
    hitbox_index_selected=list_blue_hitboxes.getSelectedIndex();
    
    int x1=blue_hitboxes.get(list_blue_hitboxes.getSelectedIndex()).x1;
    int y1=blue_hitboxes.get(list_blue_hitboxes.getSelectedIndex()).y1;
    int x2=blue_hitboxes.get(list_blue_hitboxes.getSelectedIndex()).x2;
    int y2=blue_hitboxes.get(list_blue_hitboxes.getSelectedIndex()).y2;
    spinner_x1.setValue(x1);
    spinner_y1.setValue(y1);
    spinner_x2.setValue(x2);
    spinner_y2.setValue(y2);
}//GEN-LAST:event_list_blue_hitboxesMousePressed

private void list_framesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list_framesMousePressed
    current_hitbox=null;
    hitbox_index_selected=-1;
    frame_index_selected=Integer.parseInt(""+list_frames.getSelectedIndex());
    //NodeList listOfMoves = hitboxes_doc.getElementsByTagName("Move");
    
    listOfMoves_hitboxes_file = hitboxes_doc.getElementsByTagName("Move");

    boolean frame_found=false;
    
    for(int s=0; s<listOfMoves_hitboxes_file.getLength() ; s++)//Move loop
    {
        Node move_node = listOfMoves_hitboxes_file.item(s);
        Element move_element = (Element)move_node;
        if(move_element.getAttribute("name").equals(list_moves.getSelectedValue()))
        {
            for(Node frame=move_element.getFirstChild();frame!=null;frame=frame.getNextSibling())//Frame loop
            {
                if(frame.getNodeName().equals("Frame"))
                {
                    String frame_number = "frame " + ((Element)frame).getAttribute("number");
                    if( frame_number.equals((String)list_frames.getSelectedValue()) )
                    {
                        current_frame = (Element) frame;
                        updateHitboxes(current_frame);
                        frame_found=true;
                    }
                }
            }
        }
    }
    if(!frame_found)
    {
        //Clear hitboxes list
        DefaultListModel clean_model = new DefaultListModel();
        list_red_hitboxes.setModel(clean_model);
        list_blue_hitboxes.setModel(clean_model);
        blue_hitboxes.clear();
        red_hitboxes.clear();
        printSprite();
    }
}//GEN-LAST:event_list_framesMousePressed

void updateHitboxes(Element frame)
{
    blue_hitboxes.clear();
    red_hitboxes.clear();
    
    boolean empty_red = false;
    boolean empty_blue = false;
    
    for(Node hitbox=frame.getFirstChild();hitbox!=null;hitbox=hitbox.getNextSibling())//Hitbox loop
    {
        if(hitbox.getNodeName().equals("Hitboxes"))
        {
            if(((Element)hitbox).getAttribute("variable").equals("red"))
            {
                empty_red=true;
                for(Node hitbox_red=hitbox.getFirstChild();hitbox_red!=null;hitbox_red=hitbox_red.getNextSibling())//Red Hitboxes loop
                {
                    //!!!
                    if(hitbox_red.getNodeName().equals("Hitbox"))
                    {
                        int x1 = Integer.parseInt(((Element)hitbox_red).getAttribute("x1"));
                        int y1 = Integer.parseInt(((Element)hitbox_red).getAttribute("y1"));
                        int x2 = Integer.parseInt(((Element)hitbox_red).getAttribute("x2"));
                        int y2 = Integer.parseInt(((Element)hitbox_red).getAttribute("y2"));
                        red_hitboxes.add(new Hitbox(x1, y1, x2, y2));
                        empty_red=false;
                    }
                }
            }
            if(((Element)hitbox).getAttribute("variable").equals("blue"))
            {
                empty_blue=true;
                for(Node hitbox_blue=hitbox.getFirstChild();hitbox_blue!=null;hitbox_blue=hitbox_blue.getNextSibling())//Blue Hitboxes loop
                {
                    if(hitbox_blue.getNodeName().equals("Hitbox"))
                    {
                        int x1 = Integer.parseInt(((Element)hitbox_blue).getAttribute("x1"));
                        int y1 = Integer.parseInt(((Element)hitbox_blue).getAttribute("y1"));
                        int x2 = Integer.parseInt(((Element)hitbox_blue).getAttribute("x2"));
                        int y2 = Integer.parseInt(((Element)hitbox_blue).getAttribute("y2"));
                        blue_hitboxes.add(new Hitbox(x1, y1, x2, y2));
                        empty_blue=false;
                    }
                }
            }
        }
    }
    
    drawHitboxes(empty_blue, empty_red);
    saveHitboxes();
}

void saveHitboxes()
{
    try {
        //Save XML file
        OutputFormat format = new OutputFormat(hitboxes_doc);
        format.setIndenting(true);
        XMLSerializer serializer;
        serializer = new XMLSerializer(new FileOutputStream(new File(directory_path+"/hitboxes.xml")), format);
        serializer.serialize(hitboxes_doc);
    } catch (FileNotFoundException ex) {
        Logger.getLogger(fightermakerView.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
        Logger.getLogger(fightermakerView.class.getName()).log(Level.SEVERE, null, ex);
    }
}

void drawHitboxes(boolean empty_blue,boolean empty_red)
{
    //Update hitbox_list
    DefaultListModel model_red_hitboxes = new DefaultListModel();
    int model_pos=0;
    for(int i=0;i<red_hitboxes.size();i++)
    {
        model_red_hitboxes.add(model_pos,"["+red_hitboxes.get(i).x1+","+red_hitboxes.get(i).y1+"]"+"["+red_hitboxes.get(i).x2+","+red_hitboxes.get(i).y2+"]");
        model_pos++;
    }
    
    if(empty_red)
    {
        model_red_hitboxes.add(model_pos,"Clean red hitboxes");
        model_pos++;
    }
    
    list_red_hitboxes.setModel(model_red_hitboxes);
    
    DefaultListModel model_blue_hitboxes = new DefaultListModel();
    model_pos=0;
    for(int i=0;i<blue_hitboxes.size();i++)
    {
        model_blue_hitboxes.add(model_pos,"["+blue_hitboxes.get(i).x1+","+blue_hitboxes.get(i).y1+"]"+"["+blue_hitboxes.get(i).x2+","+blue_hitboxes.get(i).y2+"]");
        model_pos++;
    }
    
    if(empty_blue)
    {
        model_blue_hitboxes.add(model_pos,"Clean blue hitboxes");
        model_pos++;
    }
    
    list_blue_hitboxes.setModel(model_blue_hitboxes);
    
    printSprite();
}

void printSprite()
{
    //Get the current sprite path

    NodeList listOfMovesSprites = sprites_doc.getElementsByTagName("Move");
    
    boolean sprite_found=false;

    for(int s=0; s<listOfMovesSprites.getLength() ; s++)
    {
        Node move_node = listOfMovesSprites.item(s);
        Element move_element = (Element)move_node;
        if(move_element.getAttribute("name").equals(list_moves.getSelectedValue()))
        {
            for(Node sprite=move_node.getFirstChild();sprite!=null;sprite=sprite.getNextSibling())//Sprite loop
            {
                if(sprite.getNodeName().equals("Sprite"))
                {
                    String frame_number = "frame " + ((Element)sprite).getAttribute("frame_number");
                    if(frame_number.equals((String)list_frames.getSelectedValue()) )
                    {
                        sprite_found=true;
                        String sprite_path="/"+((Element)sprite).getAttribute("path");
                        //Print sprite
                        ((ImagePanel)image_panel).setImage(directory_path+sprite_path, blue_hitboxes, red_hitboxes);
                        label_current_sprite.setText(sprite_path);
                    }
                }
            }
        }
    }
    if(!sprite_found)
    {
        ((ImagePanel)image_panel).setImage("LogoEngine.png");
        label_current_sprite.setText("LogoEngine.png");
    }
}

private void list_red_hitboxesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list_red_hitboxesMousePressed
    if(blue_hitboxes.size()<=list_red_hitboxes.getSelectedIndex())
    {
        return;
    }
    hitbox_color_selected="red";
    hitbox_index_selected=list_red_hitboxes.getSelectedIndex();
    int x1=red_hitboxes.get(list_red_hitboxes.getSelectedIndex()).x1;
    int y1=red_hitboxes.get(list_red_hitboxes.getSelectedIndex()).y1;
    int x2=red_hitboxes.get(list_red_hitboxes.getSelectedIndex()).x2;
    int y2=red_hitboxes.get(list_red_hitboxes.getSelectedIndex()).y2;
    spinner_x1.setValue(x1);
    spinner_y1.setValue(y1);
    spinner_x2.setValue(x2);
    spinner_y2.setValue(y2);
}//GEN-LAST:event_list_red_hitboxesMousePressed

private void image_panelMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_image_panelMouseDragged
    // TODO add your handling code here:
    //((ImagePanel)this.jPanel1).setLocation(evt.getX(), evt.getY());
//    ((ImagePanel)this.image_panel).x=evt.getX();
//    ((ImagePanel)this.image_panel).y=evt.getY();
//    ((ImagePanel)this.image_panel).repaint();
}//GEN-LAST:event_image_panelMouseDragged

private void image_panelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_image_panelMouseClicked
    // TODO add your handling code here:
}//GEN-LAST:event_image_panelMouseClicked

private void image_panelMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_image_panelMouseWheelMoved

//    if(evt.getWheelRotation()<0) {
//        ((ImagePanel)this.image_panel).scale-=0.01;
//    }else {
//        ((ImagePanel)this.image_panel).scale+=0.01;
//    }
//    ((ImagePanel)this.image_panel).repaint();
}//GEN-LAST:event_image_panelMouseWheelMoved

private void spinner_x1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_spinner_x1PropertyChange

}//GEN-LAST:event_spinner_x1PropertyChange

private void spinner_x1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinner_x1StateChanged
    if(hitbox_index_selected!=-1)
    {    
        int i=0;
        for(Node hitboxes_node=current_frame.getFirstChild();hitboxes_node!=null;hitboxes_node=hitboxes_node.getNextSibling())//Hitbox loop
        {
            if(hitboxes_node.getNodeName().equals("Hitboxes"))
            {
                if(((Element)hitboxes_node).getAttribute("variable").equals(hitbox_color_selected))
                {
                    for(Node hitbox=hitboxes_node.getFirstChild();hitbox!=null;hitbox=hitbox.getNextSibling())//Red Hitboxes loop
                    {
                        if(hitbox.getNodeName().equals("Hitbox"))
                        {
                            if(i==hitbox_index_selected)
                                ((Element)hitbox).setAttribute("x1", ""+spinner_x1.getValue());
                            i++;
                        }
                    }
                }
            }
        }

        updateHitboxes(current_frame);
    }else
    {
        JOptionPane.showMessageDialog(null, "Select a hitbox first.", "Be careful", JOptionPane.INFORMATION_MESSAGE);
    }
}//GEN-LAST:event_spinner_x1StateChanged

private void spinner_y1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinner_y1StateChanged
    if(hitbox_index_selected!=-1)
    {    
        int i=0;
        for(Node hitboxes_node=current_frame.getFirstChild();hitboxes_node!=null;hitboxes_node=hitboxes_node.getNextSibling())//Hitbox loop
        {
            if(hitboxes_node.getNodeName().equals("Hitboxes"))
            {
                if(((Element)hitboxes_node).getAttribute("variable").equals(hitbox_color_selected))
                {
                    for(Node hitbox=hitboxes_node.getFirstChild();hitbox!=null;hitbox=hitbox.getNextSibling())//Red Hitboxes loop
                    {
                        if(hitbox.getNodeName().equals("Hitbox"))
                        {
                            if(i==hitbox_index_selected)
                                ((Element)hitbox).setAttribute("y1", ""+spinner_y1.getValue());
                            i++;
                        }
                    }
                }
            }
        }

        updateHitboxes(current_frame);
    }else
    {
        JOptionPane.showMessageDialog(null, "Select a hitbox first.", "Be careful", JOptionPane.INFORMATION_MESSAGE);
    }
}//GEN-LAST:event_spinner_y1StateChanged

private void spinner_x2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinner_x2StateChanged
    if(hitbox_index_selected!=-1)
    {    
        int i=0;
        for(Node hitboxes_node=current_frame.getFirstChild();hitboxes_node!=null;hitboxes_node=hitboxes_node.getNextSibling())//Hitbox loop
        {
            if(hitboxes_node.getNodeName().equals("Hitboxes"))
            {
                if(((Element)hitboxes_node).getAttribute("variable").equals(hitbox_color_selected))
                {
                    for(Node hitbox=hitboxes_node.getFirstChild();hitbox!=null;hitbox=hitbox.getNextSibling())//Red Hitboxes loop
                    {
                        if(hitbox.getNodeName().equals("Hitbox"))
                        {
                            if(i==hitbox_index_selected)
                                ((Element)hitbox).setAttribute("x2", ""+spinner_x2.getValue());
                            i++;
                        }
                    }
                }
            }
        }

        updateHitboxes(current_frame);
    }else
    {
        JOptionPane.showMessageDialog(null, "Select a hitbox first.", "Be careful", JOptionPane.INFORMATION_MESSAGE);
    }
}//GEN-LAST:event_spinner_x2StateChanged

private void spinner_y2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinner_y2StateChanged
    if(hitbox_index_selected!=-1)
    {    
        int i=0;
        for(Node hitboxes_node=current_frame.getFirstChild();hitboxes_node!=null;hitboxes_node=hitboxes_node.getNextSibling())//Hitbox loop
        {
            if(hitboxes_node.getNodeName().equals("Hitboxes"))
            {
                if(((Element)hitboxes_node).getAttribute("variable").equals(hitbox_color_selected))
                {
                    for(Node hitbox=hitboxes_node.getFirstChild();hitbox!=null;hitbox=hitbox.getNextSibling())//Red Hitboxes loop
                    {
                        if(hitbox.getNodeName().equals("Hitbox"))
                        {
                            if(i==hitbox_index_selected)
                                ((Element)hitbox).setAttribute("y2", ""+spinner_y2.getValue());
                            i++;
                        }
                    }
                }
            }
        }

        updateHitboxes(current_frame);
    }else
    {
        JOptionPane.showMessageDialog(null, "Select a hitbox first.", "Be careful", JOptionPane.INFORMATION_MESSAGE);
    }
}//GEN-LAST:event_spinner_y2StateChanged

private void button_add_red_hitboxMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_add_red_hitboxMousePressed
    if(frame_index_selected!=-1)
    {
        if(current_frame==null)
        {
            Element new_frame = hitboxes_doc.createElement("Frame");
            new_frame.setAttribute("number", ""+(frame_index_selected+1));
            if(current_move==null)
            {
                Element new_move = hitboxes_doc.createElement("Move");
                new_move.setAttribute("name", ""+list_moves.getSelectedValue());
                hitboxes_doc.getFirstChild().appendChild(new_move);
                current_move=new_move;
            }
            current_move.appendChild(new_frame);
            current_frame=new_frame;
        }
        int i=0;
        boolean hitboxes_exist=false;
        for(Node hitboxes_node=current_frame.getFirstChild();hitboxes_node!=null;hitboxes_node=hitboxes_node.getNextSibling())//Hitbox loop
        {
            if(hitboxes_node.getNodeName().equals("Hitboxes"))
            {
                if(((Element)hitboxes_node).getAttribute("variable").equals("red"))
                {
                    hitboxes_exist=true;
                    Element new_hitbox = hitboxes_doc.createElement("Hitbox");
                    new_hitbox.setAttribute("x1", ""+spinner_x1.getValue());
                    new_hitbox.setAttribute("y1", ""+spinner_y1.getValue());
                    new_hitbox.setAttribute("x2", ""+spinner_x2.getValue());
                    new_hitbox.setAttribute("y2", ""+spinner_y2.getValue());
                    hitboxes_node.appendChild(new_hitbox);
                }
            }
        }
        
        if(!hitboxes_exist)
            JOptionPane.showMessageDialog(null, "Create a clean hitboxes list before.", "Be careful", JOptionPane.INFORMATION_MESSAGE);
        
        updateHitboxes(current_frame);
    }
    else
    {
        JOptionPane.showMessageDialog(null, "Select a frame first.", "Be careful", JOptionPane.INFORMATION_MESSAGE);
    }
}//GEN-LAST:event_button_add_red_hitboxMousePressed

private void button_add_blue_hitboxMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_add_blue_hitboxMousePressed

    if(frame_index_selected!=-1)
    {
        if(current_frame==null)
        {
            Element new_frame = hitboxes_doc.createElement("Frame");
            new_frame.setAttribute("number", ""+(frame_index_selected+1));
            if(current_move==null)
            {
                Element new_move = hitboxes_doc.createElement("Move");
                new_move.setAttribute("name", ""+list_moves.getSelectedValue());
                hitboxes_doc.getFirstChild().appendChild(new_move);
                current_move=new_move;
            }
            current_move.appendChild(new_frame);
            current_frame=new_frame;
        }
        int i=0;
        boolean hitboxes_exist=false;
        for(Node hitboxes_node=current_frame.getFirstChild();hitboxes_node!=null;hitboxes_node=hitboxes_node.getNextSibling())//Hitbox loop
        {
            if(hitboxes_node.getNodeName().equals("Hitboxes"))
            {
                if(((Element)hitboxes_node).getAttribute("variable").equals("blue"))
                {
                    hitboxes_exist=true;
                    Element new_hitbox = hitboxes_doc.createElement("Hitbox");
                    new_hitbox.setAttribute("x1", ""+spinner_x1.getValue());
                    new_hitbox.setAttribute("y1", ""+spinner_y1.getValue());
                    new_hitbox.setAttribute("x2", ""+spinner_x2.getValue());
                    new_hitbox.setAttribute("y2", ""+spinner_y2.getValue());
                    hitboxes_node.appendChild(new_hitbox);
                }
            }
        }
        
        if(!hitboxes_exist)
            JOptionPane.showMessageDialog(null, "Create a clean hitboxes list before.", "Be careful", JOptionPane.INFORMATION_MESSAGE);
        
        updateHitboxes(current_frame);
    }
    else
    {
        JOptionPane.showMessageDialog(null, "Select a frame first.", "Be careful", JOptionPane.INFORMATION_MESSAGE);
    }
    
}//GEN-LAST:event_button_add_blue_hitboxMousePressed

private void button_delete_selectedMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_delete_selectedMousePressed
    if(hitbox_index_selected!=-1)
    {
        int i=0;
        for(Node hitboxes_node=current_frame.getFirstChild();hitboxes_node!=null;hitboxes_node=hitboxes_node.getNextSibling())//Hitbox loop
        {
            if(hitboxes_node.getNodeName().equals("Hitboxes"))
            {
                if(((Element)hitboxes_node).getAttribute("variable").equals(hitbox_color_selected))
                {
                    for(Node hitbox=hitboxes_node.getFirstChild();hitbox!=null;hitbox=hitbox.getNextSibling())//Red Hitboxes loop
                    {
                        if(hitbox.getNodeName().equals("Hitbox"))
                        {
                            if(i==hitbox_index_selected)
                            {
                                hitbox.getParentNode().removeChild(hitbox);
                                break;
                            }
                            i++;
                        }
                    }
                }
            }
        }

        updateHitboxes(current_frame);
    }
    else
    {
        JOptionPane.showMessageDialog(null, "Select a hitbox in a frame first.", "Be careful", JOptionPane.INFORMATION_MESSAGE);
    }
}//GEN-LAST:event_button_delete_selectedMousePressed

private void button_clean_blue_listMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_clean_blue_listMousePressed
    if(frame_index_selected!=-1)
    {
        if(current_frame==null)
        {
            Element new_frame = hitboxes_doc.createElement("Frame");
            new_frame.setAttribute("number", ""+(frame_index_selected+1));
            if(current_move==null)
            {
                Element new_move = hitboxes_doc.createElement("Move");
                new_move.setAttribute("name", ""+list_moves.getSelectedValue());
                hitboxes_doc.getFirstChild().appendChild(new_move);
                current_move=new_move;
            }
            current_move.appendChild(new_frame);
            current_frame=new_frame;
        }
        int i=0;
        boolean hitboxes_exist=false;
        for(Node hitboxes_node=current_frame.getFirstChild();hitboxes_node!=null;hitboxes_node=hitboxes_node.getNextSibling())//Hitbox loop
        {
            if(hitboxes_node.getNodeName().equals("Hitboxes"))
            {
                if(((Element)hitboxes_node).getAttribute("variable").equals("blue"))
                {
                    hitboxes_exist=true;
                    while(hitboxes_node.hasChildNodes())
                        hitboxes_node.removeChild(hitboxes_node.getFirstChild());
                }
            }
        }
        
        if(!hitboxes_exist)
        {
            Element new_hitboxes=hitboxes_doc.createElement("Hitboxes");
            new_hitboxes.setAttribute("to_opponent", "no");
            new_hitboxes.setAttribute("variable", "blue");
            current_frame.appendChild(new_hitboxes);
            
        }
        
        updateHitboxes(current_frame);
    }
    else
    {
        JOptionPane.showMessageDialog(null, "Select a frame first.", "Be careful", JOptionPane.INFORMATION_MESSAGE);
    }
}//GEN-LAST:event_button_clean_blue_listMousePressed

private void button_clean_red_listMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_clean_red_listMousePressed
    if(frame_index_selected!=-1)
    {
        if(current_frame==null)
        {
            Element new_frame = hitboxes_doc.createElement("Frame");
            new_frame.setAttribute("number", ""+(frame_index_selected+1));
            if(current_move==null)
            {
                Element new_move = hitboxes_doc.createElement("Move");
                new_move.setAttribute("name", ""+list_moves.getSelectedValue());
                hitboxes_doc.getFirstChild().appendChild(new_move);
                current_move=new_move;
            }
            current_move.appendChild(new_frame);
            current_frame=new_frame;
        }
        int i=0;
        boolean hitboxes_exist=false;
        for(Node hitboxes_node=current_frame.getFirstChild();hitboxes_node!=null;hitboxes_node=hitboxes_node.getNextSibling())//Hitbox loop
        {
            if(hitboxes_node.getNodeName().equals("Hitboxes"))
            {
                if(((Element)hitboxes_node).getAttribute("variable").equals("red"))
                {
                    hitboxes_exist=true;
                    while(hitboxes_node.hasChildNodes())
                        hitboxes_node.removeChild(hitboxes_node.getFirstChild());
                }
            }
        }
        
        if(!hitboxes_exist)
        {
            Element new_hitboxes=hitboxes_doc.createElement("Hitboxes");
            new_hitboxes.setAttribute("to_opponent", "no");
            new_hitboxes.setAttribute("variable", "red");
            current_frame.appendChild(new_hitboxes);
            
        }
        
        updateHitboxes(current_frame);
    }
    else
    {
        JOptionPane.showMessageDialog(null, "Select a frame first.", "Be careful", JOptionPane.INFORMATION_MESSAGE);
    }
}//GEN-LAST:event_button_clean_red_listMousePressed

private void button_remove_blue_listMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_remove_blue_listMousePressed
    if(frame_index_selected!=-1)
    {
        if(current_frame==null)
        {
            Element new_frame = hitboxes_doc.createElement("Frame");
            new_frame.setAttribute("number", ""+(frame_index_selected+1));
            if(current_move==null)
            {
                Element new_move = hitboxes_doc.createElement("Move");
                new_move.setAttribute("name", ""+list_moves.getSelectedValue());
                hitboxes_doc.getFirstChild().appendChild(new_move);
                current_move=new_move;
            }
            current_move.appendChild(new_frame);
            current_frame=new_frame;
        }
        for(Node hitboxes_node=current_frame.getFirstChild();hitboxes_node!=null;hitboxes_node=hitboxes_node.getNextSibling())//Hitbox loop
        {
            if(hitboxes_node.getNodeName().equals("Hitboxes"))
            {
                if(((Element)hitboxes_node).getAttribute("variable").equals("blue"))
                {
                    current_frame.removeChild(hitboxes_node);
                }
            }
        }
        frame_index_selected=-1;
        updateHitboxes(current_frame);
    }
    else
    {
        JOptionPane.showMessageDialog(null, "Select a frame first.", "Be careful", JOptionPane.INFORMATION_MESSAGE);
    }
}//GEN-LAST:event_button_remove_blue_listMousePressed

private void button_remove_red_listMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_remove_red_listMousePressed
    if(frame_index_selected!=-1)
    {
        if(current_frame==null)
        {
            Element new_frame = hitboxes_doc.createElement("Frame");
            new_frame.setAttribute("number", ""+(frame_index_selected+1));
            if(current_move==null)
            {
                Element new_move = hitboxes_doc.createElement("Move");
                new_move.setAttribute("name", ""+list_moves.getSelectedValue());
                hitboxes_doc.getFirstChild().appendChild(new_move);
                current_move=new_move;
            }
            current_move.appendChild(new_frame);
            current_frame=new_frame;
        }
        for(Node hitboxes_node=current_frame.getFirstChild();hitboxes_node!=null;hitboxes_node=hitboxes_node.getNextSibling())//Hitbox loop
        {
            if(hitboxes_node.getNodeName().equals("Hitboxes"))
            {
                if(((Element)hitboxes_node).getAttribute("variable").equals("red"))
                {
                    current_frame.removeChild(hitboxes_node);
                }
            }
        }
        frame_index_selected=-1;
        updateHitboxes(current_frame);
    }
    else
    {
        JOptionPane.showMessageDialog(null, "Select a frame first.", "Be careful", JOptionPane.INFORMATION_MESSAGE);
    }
}//GEN-LAST:event_button_remove_red_listMousePressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_add_blue_hitbox;
    private javax.swing.JButton button_add_red_hitbox;
    private javax.swing.JButton button_clean_blue_list;
    private javax.swing.JButton button_clean_red_list;
    private javax.swing.JButton button_delete_selected;
    private javax.swing.JButton button_remove_blue_list;
    private javax.swing.JButton button_remove_red_list;
    private javax.swing.JPanel image_panel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JLabel label_current_sprite;
    private javax.swing.JList list_blue_hitboxes;
    private javax.swing.JList list_frames;
    private javax.swing.JList list_moves;
    private javax.swing.JList list_red_hitboxes;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JSpinner spinner_x1;
    private javax.swing.JSpinner spinner_x2;
    private javax.swing.JSpinner spinner_y1;
    private javax.swing.JSpinner spinner_y2;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;

    public void valueChanged(TreeSelectionEvent tse) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
