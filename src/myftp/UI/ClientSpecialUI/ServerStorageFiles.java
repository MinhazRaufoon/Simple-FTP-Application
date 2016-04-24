/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.UI.ClientSpecialUI;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import myftp.UI.ClientFrame;

/**
 *
 * @author RAUF00N
 */
public class ServerStorageFiles extends HddFiles{
    public ServerStorageFiles(ClientFrame clientframe)
    {
        super(clientframe);
        init_label_panel("Files at current server directory");
        init_scrollpane();
        init_scrollpanel();
        scrollpanel.setLayout(new BoxLayout(scrollpanel,BoxLayout.Y_AXIS));
        scroll.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.decode("#c1bbb3")));
    }

    @Override
    void listFiles($Directory dir) {

    }
}
