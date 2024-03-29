# Лабораторная работа 2. Фондовый рынок
## Установка
Для успешной работы программы необходимо положить актуальный драйвер Google Chrome в данную папку. Во время написания данного описания работала ссылка https://chromedriver.chromium.org/downloads для скачивания актуального драйвера.
## Условия
1. Необходимо реализовать считывание данных фондового рынка через API по заданному пользователем инструменту (например, GAZP), частотности и временному диапазону. 
Пользователь в интерфейсе программы может выбирать:

•	Название инструмента (вводить текстом или выбирать из выпадающего списка вариантов – на ваше усмотрение),

•	Частотность данных и временной диапазон, за который загружаются данные (способ задания значений определите сами – в полях интерфейса или из файла). При этом количество загружаемых значений инструмента должно быть достаточно большим, чтобы можно было выполнять расчеты на их основе.

2. Загруженные данные инструмента необходимо отображать в интерфейсе в графическом виде – с помощью графика «Японские свечи», аналогично приведенному графику.
![image](https://user-images.githubusercontent.com/49669372/195038393-7ad670bd-3565-417a-8a9b-0be4b6e0019b.png)
3. На основе данных инструмента необходимо уметь рассчитывать как минимум 2 различных торговых индикатора (разноплановых). 
При этом пользователь:

  •	Выбирает из предложенного списка нужный индикатор,

  •	Задает параметры индикатора (например, для EMA задаются период (количество измерений) N и доля использования значения цены, для MACD – задаются EMA_s, EMA_l и SMA_a, для RSI – период, уровни перекупленности и перепроданности и так далее)

4. В интерфейсе необходимо отображать график рассчитанного индикатора. 
В качестве расширения возможностей можно добавить сохранение значений индикатора в файл или отображать в виде таблицы в интерфейсе, а также возможность добавлять по несколько графиков торговых индикаторов (и удалять графики) и настраивать их взаимное расположение (на одном графике или на отдельных).

### Пример графиков индикаторов:
![image](https://user-images.githubusercontent.com/49669372/195038652-ebe5cf98-397a-4c90-a4a8-a402399ee5c8.png)
![image](https://user-images.githubusercontent.com/49669372/195038658-efa29abe-a0d5-4c25-a61d-f2adcc301427.png)
![image](https://user-images.githubusercontent.com/49669372/195038666-bd9966fa-d861-427b-8871-f47556265362.png)
![image](https://user-images.githubusercontent.com/49669372/195038677-9e6d5841-43dd-4230-945d-6db40e0b9948.png)
