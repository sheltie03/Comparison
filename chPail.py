# -*- coding: utf-8 -*-
import random
import time
import numpy
import math
from charm.toolbox.integergroup import lcm, integer
from charm.toolbox.PKEnc import PKEnc
from charm.core.engine.util import *
from charm.toolbox.integergroup import RSAGroup
from charm.schemes.pkenc.pkenc_paillier99 import Pai99


if __name__ == "__main__":
    group = RSAGroup()
    pai = Pai99(group)
    (public_key, secret_key) = pai.keygen()

# setting of plaintext
    m = 19910813
    msg = pai.encode(public_key['n'], m)

# time
    print "Average time [msec], Standard Deviation [msec]"

# time 1
    time_box = []
    for i in range(100):
        start = time.time()
        c = pai.encrypt(public_key, msg)
        elapsed_time = time.time() - start
        time_box.append(elapsed_time)
    ave = numpy.average(numpy.array(time_box))
    var = numpy.var(numpy.array(time_box))
    std = math.sqrt(var)
    print "Encryption:", ave * 1000, std * 1000

# time 2
    time_box = []
    for i in range(100):
        start = time.time()
        pai.decrypt(public_key, secret_key, c)
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
        c = pai.encrypt(public_key, msg)
        pai.decrypt(public_key, secret_key, c)
        elapsed_time = time.time() - start
        time_box.append(elapsed_time)
    ave = numpy.average(numpy.array(time_box))
    var = numpy.var(numpy.array(time_box))
    std = math.sqrt(var)
    print "Enc + Dec:", ave * 1000, std * 1000
