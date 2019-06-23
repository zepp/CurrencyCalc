# Currency Calculator

This project was started as a test to get a job in «Сбербанк-Технологии» ([Sberbank](http://www.sberbank.ru/) IT subsidiary). My Android experience was not comprehensive at that time since I used to work as embedded engineer. I just had finished several courses on [Coursera](https://www.coursera.org/) related to Android application programming. Unfortunately, it does not cover such things as MVC/MVP ideology. As a result, an initial version of my app was really awful. It is clear from the commits history. Eventually, I got a full-time job in another company but decided to finish this project. So here it is.

This app is simple currency converter or calculator, nothing more. It fetches data from [Central Bank of Russia](http://cbr.ru/) to get exchange rates. Currency amount may be typed using on-screen num pad. Also, you may swap original and resulting currency by pressing "⇅" button. Currency selection dialog shows currency description in Russian since an origin of the data provider.

Initial requirement was to develop app that has no external dependencies (except [SimpleXML](http://simple.sourceforge.net/)) so no third-party libraries can be used. I decided to follow this limitation during the further application development and made it as simple as possible. I implemented MVP from scratch so it is really simple and easy to understand. All presenter handlers and domain logic code run on single thread executor. Network requests are processed using standalone cached executor to avoid locking.

This app demonstrates:

* How to design a simple and clear UI according to MVP ideology
* How to offload main thread from time-consuming operations using ExecutorService
* How to parse XML files using [SimpleXML](http://simple.sourceforge.net/) library

Currency flags are provided by [currency-flags](https://github.com/transferwise/currency-flags)
