# -*- coding: utf-8 -*-
import random
import time
import numpy
import math
from Crypto.Util.number import *


# LCM
def LCM(p, q):
    return p * q / GCD(p, q)


# Paillier Key Generation
def PailKeyGen(p, q):
    flag = 0
    if GCD(p * q, (p - 1) * (q - 1)) != 1:
        return 0
    else:
        n = p * q
        n2 = n ** 2
        lam = LCM(p - 1, q - 1)
        while flag != 1:
            alpha = random.randint(1, n2 - 1)
            beta = random.randint(1, n2 - 1)
            if alpha % p == 0 or alpha % q == 0:
                continue
            elif beta % p == 0 or beta % q == 0:
                continue
            else:
                flag = 1
        g = ((1 + alpha * n) * pow(beta, n, n2)) % n2
        return [n, g, lam]


# Paillier Encoding
def PailEnc(m, n, g, n2):
    flag = 0
    while flag != 1:
        r = random.randint(1, n2 - 1) * 2
        if r % n == 0 or r % n == 0:
            continue
        else:
            flag = 1
    return (pow(g, m, n2) * pow(r, n, n2)) % n2


# Pailier L function
def L(x, n):
    return (x - 1) / n


# Paillier Decoding
def PailDec(c, lam, n, g, n2):
    return (L(pow(c, lam, n2), n) * inverse(L(pow(g, lam, n2), n), n)) % n


# Test
if __name__ == '__main__':
    m = 19910813
    p = 11680610569588790846719050387635150916386537519628391505052616220025069169334914985978494552300113597095435612762082695306304849762531442299009542540057807
    q = 11635960520674358921712306047987978111551452881537167964799128382131248473653748336593826937633397637078845204883116714935776408687981753746402668472432183
    key = PailKeyGen(p, q)
    n = key[0]
    g = key[1]
    lam = key[2]
    n2 = n ** 2

# time
    print "Average time [msec], Standard Deviation [msec]"

# time 1
    time_box = []
    for i in range(100):
        start = time.time()
        PailEnc(m, n, g, n2)
        elapsed_time = time.time() - start
        time_box.append(elapsed_time)
    ave = numpy.average(numpy.array(time_box))
    var = numpy.var(numpy.array(time_box))
    std = math.sqrt(var)
    print "Encryption:", ave * 1000, std * 1000

# time 2
    time_box = []
    c = PailEnc(m, n, g, n2)
    for i in range(100):
        start = time.time()
        m = PailDec(c, lam, n, g, n2)
        elapsed_time = time.time() - start
        time_box.append(elapsed_time)
    ave = numpy.average(numpy.array(time_box))
    var = numpy.var(numpy.array(time_box))
    std = math.sqrt(var)
    print "Decryption:", ave * 1000, std * 1000

# time 3
    time_box = []
    for i in range(100):
        start = time.time()
        c = PailEnc(m, n, g, n2)
        m = PailDec(c, lam, n, g, n2)
        elapsed_time = time.time() - start
        time_box.append(elapsed_time)
    ave = numpy.average(numpy.array(time_box))
    var = numpy.var(numpy.array(time_box))
    std = math.sqrt(var)
    print "Enc + Dec:", ave * 1000, std * 1000
