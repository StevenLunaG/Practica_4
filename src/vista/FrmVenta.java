package vista;

import controlador.AutoControl;
import controlador.TDA.listas.DynamicList;
import controlador.TDA.listas.Exception.EmptyException;
import controlador.VendedorControl;
import controlador.Venta.AutoArchivos;
import controlador.Venta.VendedorArchivos;
import controlador.Venta.VentaArchivos;
import controlador.VentaControl;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import modelo.Auto;
import modelo.Vendedor;
import vista.Utiles.Utilvista;
import vista.tablas.TablaVenta;

public class FrmVenta extends javax.swing.JFrame {

    private VentaControl ventaControl = new VentaControl();
    private VentaArchivos fileVenta = new VentaArchivos();
    private AutoControl autoControl = new AutoControl();
    private AutoArchivos fileAuto = new AutoArchivos();
    private VendedorControl vendedorControl = new VendedorControl();
    private VendedorArchivos fileVendedor = new VendedorArchivos();
    private TablaVenta vt = new TablaVenta();

    private void limpiar() {
        habilitar();
        try {
            Utilvista.cargarComboAutos(cbxAuto);
            Utilvista.cargarComboVendedores(cbxVendedor);
        } catch (EmptyException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        tbVenta.clearSelection();

        txtVin.setText("");
        txtPlaca.setText("");
        txtMarca.setText("");
        cbxColor.setSelectedIndex(-1);
        spnPrecio.setValue(0);

        txtDni.setText("");
        txtRuc.setText("");
        txtApellido.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");

        cbxAuto.setSelectedIndex(-1);
        cbxVendedor.setSelectedIndex(-1);
        txtFecha.setText("dd/mm/yyyy");
        
        txtParametro.setText("");
        txtParametro.setEnabled(false);

        cargarTabla();
        ventaControl.setVenta(null);
        autoControl.setAuto(null);
        vendedorControl.setVendedor(null);
    }

    public Boolean verificar(Integer var) {
        switch (var) {
            case 1:
                String flag1 = cbxColor.getSelectedItem() != null ? cbxColor.getSelectedItem().toString().trim() : "";
                return (!txtVin.getText().trim().isEmpty()
                        && !txtPlaca.getText().trim().isEmpty()
                        && !txtMarca.getText().trim().isEmpty()
                        && !flag1.isEmpty()
                        && (Integer) spnPrecio.getValue() != 0
                        || (Integer) spnPrecio.getValue() < 0);
            case 2:
                return (!txtDni.getText().trim().isEmpty()
                        && !txtRuc.getText().trim().isEmpty()
                        && !txtApellido.getText().trim().isEmpty()
                        && !txtNombre.getText().trim().isEmpty()
                        && !txtTelefono.getText().trim().isEmpty());
            case 3:
                String flag2 = cbxAuto.getSelectedItem() != null ? cbxAuto.getSelectedItem().toString().trim() : "";
                String flag3 = cbxVendedor.getSelectedItem() != null ? cbxVendedor.getSelectedItem().toString().trim() : "";
                return (!txtFecha.getText().trim().isEmpty()
                        && !flag2.isEmpty()
                        && !flag3.isEmpty());
            default:
                throw new AssertionError();
        }
    }

    private void guardar(Integer flag) throws EmptyException {
        switch (flag) {
            case 1:
                if (verificar(1)) {
                    autoControl.getAuto().setVin(txtVin.getText());
                    autoControl.getAuto().setPlaca(txtPlaca.getText());
                    autoControl.getAuto().setMarca(txtMarca.getText());
                    autoControl.getAuto().setColor(cbxColor.getSelectedItem().toString());
                    autoControl.getAuto().setPrecio((Integer) spnPrecio.getValue());
                    autoControl.getAuto().setDisponible(true);
                    if (autoControl.guardar()) {
                        fileAuto.setAuto(autoControl.getAuto());
                        fileAuto.persist();
                        JOptionPane.showMessageDialog(null, "Datos guardados");
                        limpiar();
                        autoControl.setAuto(null);
                    } else {
                        JOptionPane.showMessageDialog(null, "No se pudo guardar, hubo un error");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Falta llenar campos", "Error", JOptionPane.ERROR_MESSAGE);
                }
                break;

            case 2:
                if (verificar(2)) {
                    vendedorControl.getVendedor().setDni(txtDni.getText());
                    vendedorControl.getVendedor().setRuc(txtRuc.getText());
                    vendedorControl.getVendedor().setApellido(txtApellido.getText());
                    vendedorControl.getVendedor().setNombre(txtNombre.getText());
                    vendedorControl.getVendedor().setTelefono(txtTelefono.getText());
                    if (vendedorControl.guardar()) {
                        fileVendedor.setVendedor(vendedorControl.getVendedor());
                        fileVendedor.persist();
                        JOptionPane.showMessageDialog(null, "Datos guardados");
                        limpiar();
                        vendedorControl.setVendedor(null);
                    } else {
                        JOptionPane.showMessageDialog(null, "No se pudo guardar, hubo un error");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Falta llenar campos", "Error", JOptionPane.ERROR_MESSAGE);
                }
                break;

            case 3:
                if (verificar(3)) {
                    ventaControl.getVenta().setAuto(Utilvista.obtenerAutoControl(cbxAuto));
                    ventaControl.getVenta().setVendedor(Utilvista.obtenerVendedorControl(cbxVendedor));
                    ventaControl.getVenta().setFecha(Utilvista.convertirFecha(txtFecha.getText()));
                    if (ventaControl.guardar()) {
                        fileVenta.setVenta(ventaControl.getVenta());
                        fileVenta.persist();
                        JOptionPane.showMessageDialog(null, "Datos guardados");

                        autoControl.setAuto(fileAuto.getAutos().getInfo(ventaControl.getVenta().getAuto().getId()));
                        autoControl.getAuto().setDisponible(false);
                        fileAuto.merge(autoControl.getAuto(), autoControl.getAuto().getId());

                        limpiar();
                        ventaControl.setVenta(null);
                    } else {
                        JOptionPane.showMessageDialog(null, "No se pudo guardar, hubo un error");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Falta llenar campos", "Error", JOptionPane.ERROR_MESSAGE);
                }
                break;
            default:
                throw new AssertionError();
        }

    }

    private void cargarVista(Integer dato) {
        try {
            ventaControl.setVenta(fileVenta.getVentas().getInfo(dato));
            Vendedor vendedor = ventaControl.getVenta().getVendedor();
            Auto auto = ventaControl.getVenta().getAuto();

            txtVin.setText(auto.getVin());
            txtPlaca.setText(auto.getPlaca());
            txtMarca.setText(auto.getMarca());
            cbxColor.setSelectedItem(auto.getColor());
            spnPrecio.setValue(auto.getPrecio());

            txtDni.setText(vendedor.getDni());
            txtRuc.setText(vendedor.getRuc());
            txtApellido.setText(vendedor.getApellido());
            txtNombre.setText(vendedor.getNombre());
            txtTelefono.setText(vendedor.getTelefono());

            deshabilitar();

        } catch (Exception ex) {
            Logger.getLogger(FrmVenta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void deshabilitar() {
        txtVin.setEnabled(false);
        txtPlaca.setEnabled(false);
        txtMarca.setEnabled(false);
        cbxColor.setEnabled(false);
        spnPrecio.setEnabled(false);

        txtDni.setEnabled(false);
        txtRuc.setEnabled(false);
        txtApellido.setEnabled(false);
        txtNombre.setEnabled(false);
        txtTelefono.setEnabled(false);

        cbxVendedor.setEnabled(false);
        cbxAuto.setEnabled(false);
        txtFecha.setEnabled(false);

        btRegistrarA.setEnabled(false);
        btRegistrarVd.setEnabled(false);
        btRegistrarVt.setEnabled(false);
    }

    private void habilitar() {
        txtVin.setEnabled(true);
        txtPlaca.setEnabled(true);
        txtMarca.setEnabled(true);
        cbxColor.setEnabled(true);
        spnPrecio.setEnabled(true);

        txtDni.setEnabled(true);
        txtRuc.setEnabled(true);
        txtApellido.setEnabled(true);
        txtNombre.setEnabled(true);
        txtTelefono.setEnabled(true);

        cbxVendedor.setEnabled(true);
        cbxAuto.setEnabled(true);
        txtFecha.setEnabled(true);

        btRegistrarA.setEnabled(true);
        btRegistrarVd.setEnabled(true);
        btRegistrarVt.setEnabled(true);
    }

    private void cargarTabla() {
        vt.setVentas(fileVenta.all());
        tbVenta.setModel(vt);
        tbVenta.updateUI();
    }

    public FrmVenta() {
        initComponents();
        limpiar();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bg = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtVin = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtPlaca = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtMarca = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        btRegistrarA = new javax.swing.JButton();
        spnPrecio = new javax.swing.JSpinner();
        cbxColor = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtDni = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtRuc = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtApellido = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        btRegistrarVd = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtFecha = new javax.swing.JTextField();
        btRegistrarVt = new javax.swing.JButton();
        cbxVendedor = new javax.swing.JComboBox<>();
        cbxAuto = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbVenta = new javax.swing.JTable();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        cbxMetodo = new javax.swing.JComboBox<>();
        btBuscar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtParametro = new javax.swing.JTextField();
        cbxCriterio = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setResizable(false);

        bg.setForeground(new java.awt.Color(191, 189, 193));
        bg.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setFont(new java.awt.Font("Roboto Black", 0, 14)); // NOI18N
        jLabel1.setText("Auto");

        jLabel13.setText("VIN:");

        jLabel14.setText("Placas:");

        jLabel15.setText("Marca:");

        jLabel16.setText("Color:");

        jLabel17.setText("Precio:");

        btRegistrarA.setText("Registrar Nuevo Auto");
        btRegistrarA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRegistrarAActionPerformed(evt);
            }
        });

        cbxColor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Blanco", "Negro", "Gris", "Cafe", "Rojo", "Azul", "Verde" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtVin, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel15)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel17)
                                        .addGap(13, 13, 13)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(spnPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cbxColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(76, 76, 76))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(17, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btRegistrarA, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(132, 132, 132))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtVin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(cbxColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(spnPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btRegistrarA)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        bg.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 460, 240));

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel2.setFont(new java.awt.Font("Roboto Black", 0, 14)); // NOI18N
        jLabel2.setText("Vendedor");

        jLabel8.setText("DNI:");

        jLabel9.setText("RUC:");

        jLabel10.setText("Apellido:");

        jLabel11.setText("Nombre:");

        jLabel12.setText("Telefono:");

        btRegistrarVd.setText("Registrar Nuevo Vendedor");
        btRegistrarVd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRegistrarVdActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtDni, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(24, 24, 24)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel12)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel10)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel11)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtRuc, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jLabel2)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(157, 157, 157)
                        .addComponent(btRegistrarVd)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtDni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(txtRuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btRegistrarVd)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel3MouseEntered(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Roboto Black", 0, 14)); // NOI18N
        jLabel18.setText("Venta");

        jLabel19.setText("Auto:");

        jLabel20.setText("Vendedor:");

        txtFecha.setForeground(new java.awt.Color(153, 153, 153));
        txtFecha.setText("dd/mm/yyyy");
        txtFecha.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                txtFechaMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                txtFechaMouseMoved(evt);
            }
        });
        txtFecha.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txtFechaMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtFechaMousePressed(evt);
            }
        });

        btRegistrarVt.setText("Registrar Venta");
        btRegistrarVt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRegistrarVtActionPerformed(evt);
            }
        });

        cbxVendedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxVendedorActionPerformed(evt);
            }
        });

        jLabel3.setText("Fecha:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel18))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel19))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(cbxAuto, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(21, 21, 21)
                                .addComponent(jLabel20)
                                .addGap(18, 18, 18)
                                .addComponent(cbxVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(56, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btRegistrarVt)
                .addGap(198, 198, 198))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20)
                    .addComponent(cbxVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxAuto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btRegistrarVt)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tbVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NUM. VENTA", "FECHA", "VENDEDOR", "AUTO"
            }
        ));
        tbVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbVentaMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tbVenta);

        jLabel21.setFont(new java.awt.Font("Roboto Black", 0, 14)); // NOI18N
        jLabel21.setText("Buscar");

        jLabel22.setText("Metodo de Busqueda:");

        cbxMetodo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Lineal", "Binario" }));

        btBuscar.setText("Buscar");
        btBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBuscarActionPerformed(evt);
            }
        });

        jLabel4.setText("Criterio:");

        txtParametro.setEnabled(false);

        cbxCriterio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Num. Venta", "Vendedor (DNI)", "Auto (Placa)", "Auto (VIN)" }));
        cbxCriterio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbxCriterioMouseClicked(evt);
            }
        });
        cbxCriterio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxCriterioActionPerformed(evt);
            }
        });

        jButton1.setText("Limpiar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 513, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel22)
                        .addGap(18, 18, 18)
                        .addComponent(cbxMetodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbxCriterio, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtParametro, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(225, 225, 225)
                        .addComponent(btBuscar))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(224, 224, 224)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel21)
                .addGap(5, 5, 5)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(cbxMetodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtParametro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(cbxCriterio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addComponent(btBuscar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(bg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(6, 6, 6))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        limpiar();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tbVentaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbVentaMouseClicked
        if (String.valueOf(txtParametro.getText()).isEmpty()) {
            int fila = tbVenta.getSelectedRow();
            cargarVista(fila);
        }
    }//GEN-LAST:event_tbVentaMouseClicked

    private void cbxCriterioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxCriterioActionPerformed
        txtParametro.setEnabled(true);
    }//GEN-LAST:event_cbxCriterioActionPerformed

    private void txtFechaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFechaMousePressed
        if (txtFecha.getText().equals("dd/mm/yyyy")) {
            txtFecha.setText("");
            txtFecha.setForeground(Color.black);
        }
    }//GEN-LAST:event_txtFechaMousePressed

    private void txtFechaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFechaMouseExited

    }//GEN-LAST:event_txtFechaMouseExited

    private void txtFechaMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFechaMouseDragged

    }//GEN-LAST:event_txtFechaMouseDragged

    private void txtFechaMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFechaMouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechaMouseMoved

    private void jPanel3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseEntered
        if (String.valueOf(txtFecha.getText()).isEmpty()) {
            txtFecha.setText("dd/mm/yyyy");
            txtFecha.setForeground(Color.gray);
        }
    }//GEN-LAST:event_jPanel3MouseEntered

    private void btRegistrarAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRegistrarAActionPerformed
        try {
            guardar(1);
        } catch (EmptyException ex) {
            Logger.getLogger(FrmVenta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btRegistrarAActionPerformed

    private void btRegistrarVdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRegistrarVdActionPerformed
        try {
            guardar(2);
        } catch (EmptyException ex) {
            Logger.getLogger(FrmVenta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btRegistrarVdActionPerformed

    private void btRegistrarVtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRegistrarVtActionPerformed
        try {
            guardar(3);
        } catch (EmptyException ex) {
            Logger.getLogger(FrmVenta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btRegistrarVtActionPerformed

    private void btBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBuscarActionPerformed
        Integer tipo = 0;
        if (String.valueOf(txtParametro.getText()).isEmpty() == false) {
            if (cbxMetodo.getSelectedIndex() == 1) {
                tipo = 1;
            }
            String criterio;
            if (cbxCriterio.getSelectedIndex() == 0) {
                criterio = "Num. Venta";
            } else if (cbxCriterio.getSelectedIndex() == 1) {
                criterio = "Vendedor (DNI)";
                JOptionPane.showMessageDialog(null, "Se buscara la primera venta registrada del vendedor indicado");
            } else if (cbxCriterio.getSelectedIndex() == 2) {
                criterio = "Auto (Placa)";
            } else {
                criterio = "Auto (VIN)";
            }

            try {
                fileVenta.setVenta(fileVenta.buscar(tipo, criterio, txtParametro.getText()));
                fileVenta.setVentas(new DynamicList<>());
                fileVenta.getVentasAux().add(fileVenta.getVenta());

                vt.setVentas(fileVenta.getVentasAux());
                tbVenta.setModel(vt);
                tbVenta.updateUI();
                tbVenta.setEnabled(false);
                cargarVista(fileVenta.getVenta().getId());
            } catch (EmptyException ex) {
                Logger.getLogger(FrmVenta.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Ingrese el parametro de busqueda");
        }
    }//GEN-LAST:event_btBuscarActionPerformed

    private void cbxVendedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxVendedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxVendedorActionPerformed

    private void cbxCriterioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbxCriterioMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxCriterioMouseClicked

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
            java.util.logging.Logger.getLogger(FrmVenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmVenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmVenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmVenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmVenta().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bg;
    private javax.swing.JButton btBuscar;
    private javax.swing.JButton btRegistrarA;
    private javax.swing.JButton btRegistrarVd;
    private javax.swing.JButton btRegistrarVt;
    private javax.swing.JComboBox<String> cbxAuto;
    private javax.swing.JComboBox<String> cbxColor;
    private javax.swing.JComboBox<String> cbxCriterio;
    private javax.swing.JComboBox<String> cbxMetodo;
    private javax.swing.JComboBox<String> cbxVendedor;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSpinner spnPrecio;
    private javax.swing.JTable tbVenta;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtDni;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JTextField txtMarca;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtParametro;
    private javax.swing.JTextField txtPlaca;
    private javax.swing.JTextField txtRuc;
    private javax.swing.JTextField txtTelefono;
    private javax.swing.JTextField txtVin;
    // End of variables declaration//GEN-END:variables
}
