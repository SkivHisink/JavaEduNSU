package com.exlabwork2;


import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.ColumnArrangement;
import org.jfree.chart.block.LineBorder;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.ui.HorizontalAlignment;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.VerticalAlignment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MainControl {

    JComboBox<String> dataSourceComboBox;
    NasdaqApiClient api;
    JFrame frame;
    JButton connectButton;
    JButton getButton;
    JTextField startDateTextField;
    JTextField TSTextField;
    JTextField SFTextField;
    JTextField FastTSTextField;
    JTextField SlowTSTextField;
    JTextField endDateTextField;
    JComboBox dataTransporterComboBox;
    JTextField dataTransporterTextField;
    JComboBox instrumentComboBox;
    JTextField instrumentTextField;
    boolean isAlternative = false;
    ChartPanel plotRowPanel;
    JPanel panel;
    JCheckBox emaCheckBox;
    JCheckBox macdCheckBox;
    public MainControl() {
        frame = new JFrame("Финансовый обозреватель");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 200);

        frame.setMinimumSize(new Dimension(1000, 200));

        panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.CENTER);

        // Создаем панели для каждой строки и устанавливаем менеджеры компоновки
        JPanel firstRowPanel = new JPanel(new FlowLayout());
        JPanel secondRowPanel = new JPanel(new FlowLayout());
        JPanel thirdRowPanel = new JPanel(new FlowLayout());
        JPanel fourthRowPanel = new JPanel(new FlowLayout());
        JPanel fiveRowPanel = new JPanel(new FlowLayout());
        JPanel sixRowPanel = new JPanel(new FlowLayout());

        // Добавляем панели на основную панель
        panel.add(firstRowPanel);
        panel.add(secondRowPanel);
        panel.add(thirdRowPanel);
        panel.add(fourthRowPanel);
        panel.add(fiveRowPanel);
        panel.add(sixRowPanel);
        // Добавляем компоненты на каждую строку
        placeComponentsForFirstRow(firstRowPanel);
        if (isAlternative) {
            placeComponentsAlternativeForSecondRow(secondRowPanel);
            placeComponentsAlternativeForThirdRow(thirdRowPanel);
        } else {
            // Большое кол-во датасетов не имеют ни открытие, ни закрытие
            //placeComponentsForSecondRow(secondRowPanel);
            placeComponentsForThirdRow(thirdRowPanel);
            placeComponentsForFourthRow(fourthRowPanel);
        }
        placeComponentsForFiveRow(fiveRowPanel);
        placeComponentsForSixRow(sixRowPanel);
        frame.setResizable(true);
        frame.setVisible(true);

        api = new NasdaqApiClient();
    }

    List<NasdaqDatabase> nasdaqDatabaseList = null;
    List<NasdaqDataset> nasdaqDatasetList = null;

    private void placeComponentsForFiveRow(JPanel panel) {
        emaCheckBox= new JCheckBox("Включить EMA");
        panel.add(emaCheckBox);
        emaCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // При изменении состояния чекбокса, включаем/выключаем текстовое поле
                TSTextField.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
                SFTextField.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        JLabel tsLabel = new JLabel("TS:");
        panel.add(tsLabel);
        TSTextField = new JTextField(10);
        panel.add(TSTextField);
        JLabel sfLabel = new JLabel("SF:");
        panel.add(sfLabel);
        SFTextField = new JTextField(10);
        panel.add(SFTextField);
    }

    private void placeComponentsForSixRow(JPanel panel) {
        macdCheckBox= new JCheckBox("Включить MACD");
        panel.add(macdCheckBox);
        macdCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // При изменении состояния чекбокса, включаем/выключаем текстовое поле
                FastTSTextField.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
                SlowTSTextField.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        JLabel fastTsLabel = new JLabel("FastTS:");
        panel.add(fastTsLabel);

        FastTSTextField = new JTextField(10);
        panel.add(FastTSTextField);

        JLabel slowTsLabel = new JLabel("SlowTS:");
        panel.add(slowTsLabel);

        SlowTSTextField = new JTextField(10);
        panel.add(SlowTSTextField);
    }

    private void placeComponentsForFirstRow(JPanel panel) {
        JLabel label = new JLabel("Источник данных:");
        panel.add(label);

        String[] comboBoxValues = {"Не выбрано", "Nasdaq"};
        dataSourceComboBox = new JComboBox<>(comboBoxValues);
        panel.add(dataSourceComboBox);

        connectButton = new JButton("Получить");
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = (String) (dataSourceComboBox.getSelectedItem());
                if (selected.equals("Nasdaq")) {
                    connectButton.setEnabled(false);
                    if (nasdaqDatasetList == null) {
                        // Вынужденная мера
                        var data = NasdaqApiClient.getDataSets("WIKI");
                        if (data == null) {
                            JOptionPane.showMessageDialog(frame,
                                    "Не удалось загрузить данные!",
                                    "Проблема!",
                                    JOptionPane.INFORMATION_MESSAGE);
                            connectButton.setEnabled(true);
                            return;
                        }
                        // Вынужденная мера
                        //nasdaqDatabaseList = data;
                        nasdaqDatasetList = data;
                    }
                    //var nameList = nasdaqDatabaseList.stream()
                    var nameList = nasdaqDatasetList.stream()
                            //.map(NasdaqDatabase::getName)
                            .map(NasdaqDataset::getName)
                            .collect(Collectors.toList());
                    //dataTransporterComboBox.removeAllItems();
                    instrumentComboBox.removeAllItems();
                    for (var item : nameList) {
                        //dataTransporterComboBox.addItem(item);
                        instrumentComboBox.addItem(item);
                    }
                    connectButton.setEnabled(true);
                } else {

                    JOptionPane.showMessageDialog(frame,
                            "Вы выбрали нерабочий вариант источника данных!",
                            "Проблема!",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        panel.add(connectButton);
    }

    private void placeComponentsForSecondRow(JPanel panel) {
        JLabel label = new JLabel("Поставщик данных:");
        panel.add(label);
        String[] comboBoxValues = {"Не выбрано"};
        dataTransporterComboBox = new JComboBox<>(comboBoxValues);
        panel.add(dataTransporterComboBox);
        dataTransporterComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // Проверяем, что событие - выбор элемента
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // Получаем выбранный элемент
                    String selectedValue = (String) e.getItem();
                    if (selectedValue.equals("Не выбрано")) {
                        return;
                    }
                    Optional<NasdaqDatabase> foundSpec = nasdaqDatabaseList.stream()
                            .filter(spec -> spec.getName().equals(selectedValue))
                            .findFirst();

                    // Проверяем, был ли найден элемент
                    if (foundSpec.isPresent()) {
                        NasdaqDatabase spec = foundSpec.get();
                        System.out.println("Элемент найден: " + spec.getName());
                        nasdaqDatasetList = NasdaqApiClient.getDataSets(spec);
                        var nameList = nasdaqDatasetList.stream()
                                .map(NasdaqDataset::getName)
                                .collect(Collectors.toList());
                        instrumentComboBox.removeAllItems();
                        for (var item : nameList) {
                            instrumentComboBox.addItem(item);
                        }
                    } else {
                        System.out.println("Элемент с именем " + selectedValue + " не найден");
                        return;
                    }

                }
            }
        });
    }

    private void placeComponentsAlternativeForSecondRow(JPanel panel) {
        JLabel label = new JLabel("Поставщик данных:");
        panel.add(label);
        dataTransporterTextField = new JTextField(10);
        panel.add(dataTransporterTextField);
    }

    private void placeComponentsAlternativeForThirdRow(JPanel panel) {
        JLabel label = new JLabel("Поставщик данных:");
        panel.add(label);
        instrumentTextField = new JTextField(10);
        panel.add(instrumentTextField);
    }

    private void wakeUpMsgBox(String massage) {
        JOptionPane.showMessageDialog(frame,
                massage,
                "Проблема!",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void placeComponentsForThirdRow(JPanel panel) {

        JLabel label2 = new JLabel("Инструмент:");
        panel.add(label2);
        String[] comboBoxValues = {"Не выбрано"};

        instrumentComboBox = new JComboBox<>(comboBoxValues);
        instrumentComboBox.
                addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (e.getStateChange() == ItemEvent.SELECTED) {
                            // Получаем выбранный элемент
                            String selectedValue = (String) e.getItem();
                            if (selectedValue.equals("Не выбрано")) {
                                getButton.setEnabled(false);
                                return;
                            }
                            getButton.setEnabled(true);
                        }
                    }
                });
        panel.add(instrumentComboBox);
    }

    private void placeComponentsForFurthRowUniversal(JPanel panel) {
        JLabel startLabel = new JLabel("Начало периода:");
        panel.add(startLabel);

        startDateTextField = new JTextField(10);
        panel.add(startDateTextField);

        JLabel endLabel = new JLabel("Конец периода:");
        panel.add(endLabel);

        endDateTextField = new JTextField(10);
        panel.add(endDateTextField);
        getButton = new JButton("Получить данные");
        panel.add(getButton);
        getButton.setEnabled(false);
    }

    private void placeComponentsForFourthRowAlternative(JPanel panel) {
        placeComponentsForFurthRowUniversal(panel);
        getButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var dateStr = startDateTextField.getText();
                if (dateStr.length() < 8 || !isValidDateFormat(dateStr)) {
                    wakeUpMsgBox("Вы ввели начальную дату в некорректном формате. " +
                            "Правильный форма DD.MM.YYYY");
                    return;
                }
                var date1Str = dateStr;
                Date date1 = null;
                try {
                    date1 = parseDateString(dateStr);
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
                dateStr = endDateTextField.getText();
                if (dateStr.length() < 8 || !isValidDateFormat(dateStr)) {
                    wakeUpMsgBox("Вы ввели начальную дату в некорректном формате. " +
                            "Правильный форма DD.MM.YYYY");
                    return;
                }
                var date2Str = dateStr;
                Date date2 = null;
                try {
                    date2 = parseDateString(dateStr);
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
                String selectedValue = (String) instrumentComboBox.getSelectedItem();
                Optional<NasdaqDataset> foundSpec = nasdaqDatasetList.stream()
                        .filter(spec -> spec.getName().equals(selectedValue))
                        .findFirst();

                if (date1 != null && date2 != null) {
                    if (date1.equals(date2)) {
                        wakeUpMsgBox("Даты равны");
                        return;
                    } else if (date2.before(date1)) {
                        wakeUpMsgBox("Дата 1 позже даты 2");
                        return;
                    }
                } else {
                    wakeUpMsgBox("Неверный формат даты");
                    return;
                }
                // Проверяем, был ли найден элемент
                if (foundSpec.isPresent()) {
                    NasdaqDataset spec = foundSpec.get();
                    String date3Str = spec.getOldestAvailableDate();
                    if (date3Str == null || !isValidDateFormat(dateStr)) {
                        wakeUpMsgBox("Начальная дата в входных данных повреждены. Пожалуйста, обратитесь к поставщику ПО");
                    }
                    Date date3 = null;
                    try {
                        date3 = parseDateString(date3Str);
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }
                    String date4Str = spec.getNewestAvailableDate();
                    if (date4Str == null || !isValidDateFormat(dateStr)) {
                        wakeUpMsgBox("Конечная дата в входных данных повреждены. Пожалуйста, обратитесь к поставщику ПО");
                    }
                    Date date4 = null;
                    try {
                        date4 = parseDateString(date4Str);
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }
                    if (date1.before(date3)) {
                        wakeUpMsgBox("Желаемая начальная дата раньше возможной начальной даты");
                        return;
                    }
                    if (date2.before(date3)) {
                        wakeUpMsgBox("Желаемая конечная дата раньше возможной начальной даты");
                        return;
                    }
                    if (date1.after(date4)) {
                        wakeUpMsgBox("Желаемая начальная дата позже возможной конечной даты");
                        return;
                    }
                    if (date2.after(date4)) {
                        wakeUpMsgBox("Желаемая конечная дата позже возможной конечной даты");
                        return;
                    }
                    var data = NasdaqApiClient.getData(spec, date1Str, date2Str);
                    System.out.println("Well done! We have data");
                } else {
                    wakeUpMsgBox("Инструмент не найден!");
                    return;
                }
            }
        });
    }

    private void placeComponentsForFourthRow(JPanel panel) {
        placeComponentsForFurthRowUniversal(panel);
        getButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var dateStr = startDateTextField.getText();
                if (dateStr.length() < 8 || !isValidDateFormat(dateStr)) {
                    wakeUpMsgBox("Вы ввели начальную дату в некорректном формате. " +
                            "Правильный форма DD.MM.YYYY");
                    return;
                }
                var date1Str = dateStr;
                Date date1 = null;
                try {
                    date1 = parseDateString(dateStr);
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
                dateStr = endDateTextField.getText();
                if (dateStr.length() < 8 || !isValidDateFormat(dateStr)) {
                    wakeUpMsgBox("Вы ввели начальную дату в некорректном формате. " +
                            "Правильный форма DD.MM.YYYY");
                    return;
                }
                var date2Str = dateStr;
                Date date2 = null;
                try {
                    date2 = parseDateString(dateStr);
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
                if(emaCheckBox.isSelected()){
                    try {
                        double parsedDouble = Double.parseDouble(TSTextField.getText());
                    } catch (NumberFormatException ex) {
                        wakeUpMsgBox("Некорректное значение для TS");
                        return;
                    }
                    try {
                        double parsedDouble = Double.parseDouble(TSTextField.getText());
                    } catch (NumberFormatException ex) {
                        wakeUpMsgBox("Некорректное значение для SF");
                        return;
                    }
                }
                if(macdCheckBox.isSelected()){
                    try {
                        double parsedDouble = Integer.parseInt(FastTSTextField.getText());
                    } catch (NumberFormatException ex) {
                        wakeUpMsgBox("Некорректное значение для Fast TS");
                        return;
                    }
                    try {
                        double parsedDouble = Integer.parseInt(SlowTSTextField.getText());
                    } catch (NumberFormatException ex) {
                        wakeUpMsgBox("Некорректное значение для Slow TS");
                        return;
                    }
                }
                String selectedValue = (String) instrumentComboBox.getSelectedItem();
                Optional<NasdaqDataset> foundSpec = nasdaqDatasetList.stream()
                        .filter(spec -> spec.getName().equals(selectedValue))
                        .findFirst();

                if (date1 != null && date2 != null) {
                    if (date1.equals(date2)) {
                        wakeUpMsgBox("Даты равны");
                        return;
                    } else if (date2.before(date1)) {
                        wakeUpMsgBox("Дата 1 позже даты 2");
                        return;
                    }
                } else {
                    wakeUpMsgBox("Неверный формат даты");
                    return;
                }
                // Проверяем, был ли найден элемент
                if (foundSpec.isPresent()) {
                    NasdaqDataset spec = foundSpec.get();
                    String date3Str = spec.getOldestAvailableDate();
                    if (date3Str == null || !isValidDateFormat(dateStr)) {
                        wakeUpMsgBox("Начальная дата в входных данных повреждены. Пожалуйста, обратитесь к поставщику ПО");
                    }
                    Date date3 = null;
                    try {
                        date3 = parseDateString(date3Str);
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }
                    String date4Str = spec.getNewestAvailableDate();
                    if (date4Str == null || !isValidDateFormat(dateStr)) {
                        wakeUpMsgBox("Конечная дата в входных данных повреждены. Пожалуйста, обратитесь к поставщику ПО");
                    }
                    Date date4 = null;
                    try {
                        date4 = parseDateString(date4Str);
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }
                    if (date1.before(date3)) {
                        wakeUpMsgBox("Желаемая начальная дата раньше возможной начальной даты");
                        return;
                    }
                    if (date2.before(date3)) {
                        wakeUpMsgBox("Желаемая конечная дата раньше возможной начальной даты");
                        return;
                    }
                    if (date1.after(date4)) {
                        wakeUpMsgBox("Желаемая начальная дата позже возможной конечной даты");
                        return;
                    }
                    if (date2.after(date4)) {
                        wakeUpMsgBox("Желаемая конечная дата позже возможной конечной даты");
                        return;
                    }
                    var data = NasdaqApiClient.getData(spec, date1Str, date2Str);
                    JfreeCandlestickChart temp = new JfreeCandlestickChart("Plot");
                    temp.setInterval();
                    var rawDataList = data.getData();
                    var tradeDataList = new ArrayList<TradeItem>();
                    Collections.reverse(rawDataList);
                    if(rawDataList.size() > 0 && rawDataList.get(0).size()<6){
                        wakeUpMsgBox("Некорректные данные! Данные инструмент не подходит для работы!");
                        return;
                    }
                    for (int i = 0; i < rawDataList.size(); i++) {
                        var tradeItem = new TradeItem(
                                spec.getDatasetCode(),
                                60 * 24 * 7,
                                (Double) rawDataList.get(i).get(1),
                                (Double) rawDataList.get(i).get(4),
                                Math.round((Double) rawDataList.get(i).get(5)),
                                (String) rawDataList.get(i).get(0),
                                (Double) rawDataList.get(i).get(2),
                                (Double) rawDataList.get(i).get(3)
                        );
                        temp.onTrade(tradeItem);
                        tradeDataList.add(tradeItem);
                    }
                    calculateIndicators(temp, tradeDataList);
                    JFreeChart chart = temp.createChart(spec.getName());
                    LegendTitle legend = new LegendTitle(chart.getPlot(), new ColumnArrangement(HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, 0),
                            new ColumnArrangement(HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, 0));
                    legend.setPosition(RectangleEdge.BOTTOM);
                    legend.setHorizontalAlignment(HorizontalAlignment.CENTER);
                    legend.setBackgroundPaint(Color.WHITE);
                    legend.setFrame(new LineBorder());
                    legend.setMargin(0, 4, 5, 6);
                    chart.addLegend(legend);
                    if(plotRowPanel!=null) {
                        panel.remove(plotRowPanel);
                    }
                    plotRowPanel = new ChartPanel(chart);
                    panel.add(plotRowPanel);
                } else {
                    wakeUpMsgBox("Инструмент не найден!");
                    return;
                }
            }
        });
    }

    private void calculateIndicators(JfreeCandlestickChart candleStickChart, ArrayList<TradeItem> data) {
        var isEma = emaCheckBox.isSelected();
        var isMacd = macdCheckBox.isSelected();
        candleStickChart.IsEma = isEma;
        candleStickChart.IsMacd = isMacd;
        int emaTS = 0;
        double emaSF = 0;
        int macdFTS = 0;
        int macdSTS = 0;
        if (isEma) {
            emaTS = Integer.parseInt(TSTextField.getText());
            emaSF = Double.parseDouble(SFTextField.getText());
        }
        if (isMacd) {
            macdFTS = Integer.parseInt(FastTSTextField.getText());
            macdSTS = Integer.parseInt(SlowTSTextField.getText());
        }
        candleStickChart.calculateIndicator(data, emaTS, emaSF, macdFTS, macdSTS);
    }
    private Date parseDateString(String input) throws ParseException {
        SimpleDateFormat sdf;

        // Try parsing with the first format: dd.MM.yyyy
        sdf = new SimpleDateFormat("dd.MM.yyyy");
        try {
            return sdf.parse(input);
        } catch (ParseException e1) {
            sdf.applyPattern("yyyy-MM-dd");
            return sdf.parse(input);
        }
    }

    private boolean isValidDateFormat(String input) {
        String regex = "^(\\d{2}\\.\\d{2}\\.\\d{4}|\\d{4}-\\d{2}-\\d{2})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

}
