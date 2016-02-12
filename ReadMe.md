# Comparison

## 実行環境
CPUがIntel Core i5 1.3 GHz，メモリが8 GB，OSがMac OS X El Capitan 10.11.2である．

## Pai99 of Charm
　Charmは，J. A. Akinyeleらによって2013年のJournal of Cryptographic Engineeringで発表された暗号のツールである．
　ツールは主にPythonで実装されており，核心となる算術演算や楕円曲線上の加算などの速さを要求される計算は，Share Object(動的ライブラリ)によって"Charm"の'r'である"rapidly"を実現する．そのため，Pythonで実行してもCと比較できるぐらいのパフォーマンスを発揮するとAkinyeleらは主張する．
　Charmには，様々な暗号方式を実装されている．共通鍵暗号のDES, triple-DES, AESや公開鍵暗号のRSA, ElGamal, Paillierなどの半古典的な暗号方式はもちろんのこと，楕円曲線暗号やペアリング，IDベース暗号など現代の暗号方式まで実装されている．
　また，ゼロ知識に関するツールZKP Compilerも用意されている．(調査中)

```
$ pip install Charm-Crypto (Charmがなければ)
$ pip install numpy (numpyがなければ)
$ python chPaillier.py
Average time [msec], Standard Deviation [msec]
Encryption: 91.3279747963 5.88234552636
Decryption: 89.0023875237 8.50433551497
Enc + Dec: 178.580751419 17.3640960056
```

## Naive implementation of Python
 Pythonによる単純実装は，自分の研究において行ったもので，PyCryptoというライブラリを用いている．

```
$ pip install pycrypto (pycryptoがなければ)
$ pip install numpy (numpyがなければ)
$ python paillier.py
Average time [msec], Standard Deviation [msec]
Encryption: 20.7280611992 1.94788138382
Decryption: 40.2960300446 2.70888867911
Enc + Dec: 60.8372807503 2.79816149905

```


## Naive implementation of C
C言語による単純実装は，GMPライブラリを用いて行った．

```
$ brew install gmp (gmpがなければ)
(GMPとincludeをリンクさせる必要がある)
$ gcc -lgmp paillier.c
$ ./a.out
10.72
16.86
26.24
```
上から暗号化，復号，そして合計の平均時間である．時間の単位はミリ秒である．

## Naive implementation of Java
Javaによる単純実装は，研究によってAndroidでの振る舞いを実験したいと考えていた時期に行ったもので，BigIntegerを用いている．

```
$ javac paillier.java
$ java paillier
12.37
1.9629314812290315
23.9
2.1424285285628555
36.15
3.007906248539005
```
上から暗号化の平均時間，標準偏差．次に復号の平均時間，標準偏差．そして合計の平均時間，標準偏差である．時間の単位はミリ秒である．


## 比較の結果
C < Java < Python < Charm