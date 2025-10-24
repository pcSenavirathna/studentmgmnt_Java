package StudentManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.Date;

public class DatePickerDialog extends JDialog {
    private Date selectedDate;
    private Calendar cal;
    private JLabel monthLabel;
    private JPanel daysPanel;

    public DatePickerDialog(Frame owner, Date initial) {
        super(owner, "Select Date", true);
        cal = Calendar.getInstance();
        if (initial != null) cal.setTime(initial);

        setLayout(new BorderLayout());
        JPanel nav = new JPanel(new BorderLayout());
        JButton prev = new JButton("<");
        JButton next = new JButton(">");
        monthLabel = new JLabel("", SwingConstants.CENTER);
        nav.add(prev, BorderLayout.WEST);
        nav.add(monthLabel, BorderLayout.CENTER);
        nav.add(next, BorderLayout.EAST);

        prev.addActionListener(e -> { cal.add(Calendar.MONTH, -1); buildCalendar(); });
        next.addActionListener(e -> { cal.add(Calendar.MONTH, 1); buildCalendar(); });

        daysPanel = new JPanel(new GridLayout(0, 7, 4, 4));
        daysPanel.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));

        add(nav, BorderLayout.NORTH);
        add(daysPanel, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");
        bottom.add(cancel);
        bottom.add(ok);
        add(bottom, BorderLayout.SOUTH);

        ok.addActionListener(e -> {
            // if user didn't click a day, keep the calendar's current day
            if (selectedDate == null) selectedDate = cal.getTime();
            setVisible(false);
        });
        cancel.addActionListener(e -> {
            selectedDate = null;
            setVisible(false);
        });

        buildCalendar();
        pack();
        setResizable(false);
    }

    private void buildCalendar() {
        daysPanel.removeAll();

        // Day of week headers
        String[] dow = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
        for (String d : dow) {
            JLabel l = new JLabel(d, SwingConstants.CENTER);
            l.setFont(l.getFont().deriveFont(Font.BOLD, 12f));
            daysPanel.add(l);
        }

        // first day of month and number of days
        Calendar tmp = (Calendar) cal.clone();
        tmp.set(Calendar.DAY_OF_MONTH, 1);
        int firstDow = tmp.get(Calendar.DAY_OF_WEEK); // 1=Sun
        int daysInMonth = tmp.getActualMaximum(Calendar.DAY_OF_MONTH);

        // blanks before 1st
        for (int i = 1; i < firstDow; i++) daysPanel.add(new JLabel(""));

        // day buttons
        for (int d = 1; d <= daysInMonth; d++) {
            JButton b = new JButton(String.valueOf(d));
            b.setMargin(new Insets(2,2,2,2));
            final int day = d;
            b.addActionListener(e -> {
                Calendar sel = (Calendar) cal.clone();
                sel.set(Calendar.DAY_OF_MONTH, day);
                selectedDate = sel.getTime();
                // highlight selection visually by rebuilding with bold label for selected
                highlightSelected(day);
            });
            daysPanel.add(b);
        }

        monthLabel.setText(String.format("%1$tB %1$tY", cal));
        daysPanel.revalidate();
        daysPanel.repaint();
        pack();
    }

    private void highlightSelected(int day) {
        // Simple visual: rebuild and set background for selected button
        Component[] comps = daysPanel.getComponents();
        // skip first 7 header components
        for (int i = 7; i < comps.length; i++) {
            Component c = comps[i];
            if (c instanceof JButton) {
                JButton b = (JButton) c;
                if (b.getText().equals(String.valueOf(day))) {
                    b.setBackground(new Color(70,130,180));
                    b.setForeground(Color.WHITE);
                } else {
                    b.setBackground(null);
                    b.setForeground(null);
                }
            }
        }
    }

    public Date getSelectedDate() {
        return selectedDate;
    }
}