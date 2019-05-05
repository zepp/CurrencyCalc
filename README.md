# Currency Calculator

This project was started as a test to get a job in «Сбербанк-Технологии» ([Sberbank](http://www.sberbank.ru/) IT subsidiary). My Android experience was not comprehensive since I used to work as embedded engineer. I just had finished several courses on [Coursera](https://www.coursera.org/) related to Android application programming. Unfortunately, it does not cover such things as MVC/MVP ideology. As a result, an initial version of my app was really awful. It is clear from the commits history. Eventually, I got a full-time job in another company but decided to finish this project. So here it is.

This app is simple currency converter or calculator, nothing more. It fetches data from [Central Bank of Russia](http://cbr.ru/) to get exchange rates. Currency amount may be typed using on-screen num pad. Also, you may swap original and resulting currency by pressing "⇅" button. Currency selection dialog shows currency description in Russian since an origin of the data provider.

This app demonstrates:

* How to design a simple and clear UI according to MVP ideology
* How to offload main thread from time-consuming operations using ExecutorService
* How to parse XML files using [SimpleXML](http://simple.sourceforge.net/) library
* How to fetch data using [OkHttp](http://square.github.io/okhttp/) library

Currency flags are provided by [currency-flags](https://github.com/transferwise/currency-flags)
