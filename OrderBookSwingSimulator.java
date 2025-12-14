import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;


public class OrderBookSwingSimulator extends JFrame {

   

    static class Order {
        String userId;
        int qty;

        Order(String userId, int qty) {
            this.userId = userId;
            this.qty = qty;
        }
    }

    static class OrderBook {
        TreeMap<Double, Queue<Order>> buy =
                new TreeMap<>(Collections.reverseOrder());
        TreeMap<Double, Queue<Order>> sell =
                new TreeMap<>();

        int trades = 0;
        int volume = 0;
        java.util.List<String> tradeHistory = new ArrayList<>();

        void addOrder(String type, String userId, double price, int qty, String stock) {
            Order order = new Order(userId, qty);
            if (type.equals("BUY")) matchBuy(price, order, stock);
            else matchSell(price, order, stock);
        }

        private void matchBuy(double price, Order buyOrder, String stock) {
            while (buyOrder.qty > 0 && !sell.isEmpty() && sell.firstKey() <= price) {
                double sellPrice = sell.firstKey();
                Queue<Order> sq = sell.get(sellPrice);
                Order sellOrder = sq.peek();

                int traded = Math.min(buyOrder.qty, sellOrder.qty);
                buyOrder.qty -= traded;
                sellOrder.qty -= traded;

                recordTrade(stock, buyOrder.userId, sellOrder.userId, traded, sellPrice);

                if (sellOrder.qty == 0) sq.poll();
                if (sq.isEmpty()) sell.remove(sellPrice);
            }
            if (buyOrder.qty > 0)
                buy.computeIfAbsent(price, k -> new ArrayDeque<>()).add(buyOrder);
        }

        private void matchSell(double price, Order sellOrder, String stock) {
            while (sellOrder.qty > 0 && !buy.isEmpty() && buy.firstKey() >= price) {
                double buyPrice = buy.firstKey();
                Queue<Order> bq = buy.get(buyPrice);
                Order buyOrder = bq.peek();

                int traded = Math.min(sellOrder.qty, buyOrder.qty);
                sellOrder.qty -= traded;
                buyOrder.qty -= traded;

                recordTrade(stock, buyOrder.userId, sellOrder.userId, traded, buyPrice);

                if (buyOrder.qty == 0) bq.poll();
                if (bq.isEmpty()) buy.remove(buyPrice);
            }
            if (sellOrder.qty > 0)
                sell.computeIfAbsent(price, k -> new ArrayDeque<>()).add(sellOrder);
        }

        private void recordTrade(String stock, String buyer, String seller, int qty, double price) {
            trades++;
            volume += qty;
            tradeHistory.add(
                    stock + " | BUYER: " + buyer +
                    " | SELLER: " + seller +
                    " | QTY: " + qty +
                    " @ " + price
            );
        }
    }

    /* ======================= DATA ======================= */

    private final Map<String, Double> stockBasePrice = Map.of(
            "Google", 2800.0,
            "Facebook", 350.0,
            "Tesla", 900.0,
            "Reliance", 2500.0,
            "Mahindra", 1600.0
    );

    private final Map<String, OrderBook> stockBooks = new HashMap<>();

    /* ======================= UI COMPONENTS ======================= */

    private final JTextField userField = new JTextField("User1");
    private final JTextField priceField = new JTextField();
    private final JTextField qtyField = new JTextField("1");

    private final JComboBox<String> stockBox =
            new JComboBox<>(stockBasePrice.keySet().toArray(new String[0]));

    private final DefaultTableModel buyModel =
            new DefaultTableModel(new String[]{"Price", "Orders"}, 0);
    private final DefaultTableModel sellModel =
            new DefaultTableModel(new String[]{"Price", "Orders"}, 0);

    private final DefaultListModel<String> historyModel = new DefaultListModel<>();
    private final JLabel spreadLabel = new JLabel("", SwingConstants.CENTER);

    public OrderBookSwingSimulator() {

        stockBasePrice.forEach((k, v) -> stockBooks.put(k, new OrderBook()));

        setTitle("Multi-Stock Order Book Simulator");
        setSize(1250, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));

        add(leftPanel(), BorderLayout.WEST);
        add(centerPanel(), BorderLayout.CENTER);
        add(rightPanel(), BorderLayout.EAST);

        stockBox.addActionListener(e -> updatePrice());

        updatePrice();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /* ======================= PANELS ======================= */

    private JPanel leftPanel() {
        JPanel p = card("Place Order");
        p.setPreferredSize(new Dimension(280, 0));
        p.setLayout(new GridLayout(0, 1, 8, 8));

        p.add(new JLabel("User ID"));
        p.add(userField);

        p.add(new JLabel("Stock"));
        p.add(stockBox);

        p.add(new JLabel("Price"));
        p.add(priceField);

        p.add(new JLabel("Quantity"));
        p.add(qtyField);

        JButton buyBtn = button("BUY", new Color(22,185,130));
        JButton sellBtn = button("SELL", new Color(239,68,68));

        buyBtn.addActionListener(e -> place("BUY"));
        sellBtn.addActionListener(e -> place("SELL"));

        p.add(buyBtn);
        p.add(sellBtn);

        return p;
    }

    private JPanel centerPanel() {
        JPanel p = card("Order Book");
        p.setLayout(new BorderLayout(10,10));

        p.add(new JScrollPane(new JTable(sellModel)), BorderLayout.NORTH);

        spreadLabel.setOpaque(true);
        spreadLabel.setBackground(new Color(99,102,241));
        spreadLabel.setForeground(Color.WHITE);
        p.add(spreadLabel, BorderLayout.CENTER);

        p.add(new JScrollPane(new JTable(buyModel)), BorderLayout.SOUTH);
        return p;
    }

    private JPanel rightPanel() {
        JPanel p = card("Trade History");
        p.setLayout(new BorderLayout());
        p.setPreferredSize(new Dimension(350,0));
        p.add(new JScrollPane(new JList<>(historyModel)));
        return p;
    }

    /* ======================= HELPERS ======================= */

    private JPanel card(String title) {
        JPanel p = new JPanel();
        p.setBorder(new CompoundBorder(
                new EmptyBorder(10,10,10,10),
                new TitledBorder(title)));
        return p;
    }

    private JButton button(String text, Color c) {
        JButton b = new JButton(text);
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        return b;
    }

    private void updatePrice() {
        String stock = (String) stockBox.getSelectedItem();
        priceField.setText(stockBasePrice.get(stock).toString());
        refreshUI();
    }

    private void place(String type) {
        try {
            String user = userField.getText();
            String stock = (String) stockBox.getSelectedItem();
            double price = Double.parseDouble(priceField.getText());
            int qty = Integer.parseInt(qtyField.getText());

            stockBooks.get(stock).addOrder(type, user, price, qty, stock);
            refreshUI();
        } catch (Exception ignored) {}
    }

    private void refreshUI() {
        String stock = (String) stockBox.getSelectedItem();
        OrderBook book = stockBooks.get(stock);

        buyModel.setRowCount(0);
        sellModel.setRowCount(0);
        historyModel.clear();

        book.buy.forEach((p,q)-> buyModel.addRow(new Object[]{p,q.size()}));
        book.sell.forEach((p,q)-> sellModel.addRow(new Object[]{p,q.size()}));
        book.tradeHistory.forEach(historyModel::addElement);

        double bid = book.buy.isEmpty()?0:book.buy.firstKey();
        double ask = book.sell.isEmpty()?0:book.sell.firstKey();
        spreadLabel.setText("Stock: " + stock + " | Spread: " + (ask-bid));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OrderBookSwingSimulator::new);
    }
}
