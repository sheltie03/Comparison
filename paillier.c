#include <stdio.h>
#include <gmp.h>
#include <time.h>

#define BASE 10


void PailSKeyGen(mpz_t p, mpz_t q, mpz_t lam, mpz_t one) {
// Setting
	mpz_t pminus, qminus;
	mpz_init(lam);
	mpz_init(pminus);
	mpz_init(qminus);

// Secret key caluculation
	mpz_sub(pminus, p, one);
	mpz_sub(qminus, q, one);
	mpz_lcm(lam, pminus, qminus);

// Memory Release
	mpz_clear(one);
	mpz_clear(pminus);
	mpz_clear(qminus);
}


void PailEnc(mpz_t m, mpz_t n, mpz_t g, mpz_t n2, mpz_t seed, mpz_t c) {
// Setting
	size_t size;
	mpz_t rnd, tmp1, tmp2;
	mpz_init(rnd);
	mpz_init(tmp1);
	mpz_init(tmp2);

// Setting of Random Integer	
	gmp_randstate_t state;
	gmp_randinit_default(state);
	mp_bitcnt_t nbit = 100; // You must change the size of random integer.
	gmp_randseed(state, seed);
	mpz_urandomb(rnd, state, nbit);

// Decoding
	mpz_powm(tmp1, rnd, n, n2);
	mpz_powm(tmp2, g, m, n2);
	mpz_mul(tmp1, tmp1, tmp2);
	mpz_mod(c, tmp1, n2);

// Memory Release
	mpz_clear(rnd);
	mpz_clear(tmp1);
	mpz_clear(tmp2);
}


void PailDec(mpz_t c, mpz_t lam, mpz_t n, mpz_t g, mpz_t n2, mpz_t one, mpz_t m) {
// Setting
	mpz_t tmp, tmp2;
	mpz_init(tmp);
	mpz_init(tmp2);

// Encoding message	
	mpz_powm(tmp, c, lam, n2);
	mpz_sub(tmp, tmp, one);
	mpz_tdiv_q(tmp, tmp, n);

// Encoding random integer 
	mpz_powm(tmp2, g, lam, n2);
	mpz_sub(tmp2, tmp2, one);
	mpz_tdiv_q(tmp2, tmp2, n);
	mpz_invert(tmp2, tmp2, n);

// Fusion	
	mpz_mul(tmp, tmp, tmp2);
	mpz_mod(m, tmp, n);

// Memory Release
	mpz_clear(tmp);
	mpz_clear(tmp2);
}


int main(void) {
	clock_t start, end;
	mpz_t p, q, lam, n, n2, g, one, m, c, seed;
	mpz_init(p);
	mpz_init(q);
	mpz_init(lam);
	mpz_init(g);
	mpz_init(n);
	mpz_init(n2);
	mpz_init(one);
	mpz_init(m);
	mpz_init(c);
	mpz_init(seed);

// Constant value of 1
	mpz_set_str(one, "1", BASE);

	mpz_set_str(p, "11680610569588790846719050387635150916386537519628391505052616220025069169334914985978494552300113597095435612762082695306304849762531442299009542540057807", BASE);
	mpz_set_str(q, "11635960520674358921712306047987978111551452881537167964799128382131248473653748336593826937633397637078845204883116714935776408687981753746402668472432183", BASE);

// Paillier Public Key
	mpz_mul(n, p, q);
	mpz_mul(n2, n, n);
	mpz_add(g, n, one);

// Paillier Secret Key
        PailSKeyGen(p, q, lam, one);
//	mpz_out_str(stdout, BASE, lam);
//	printf("\n");

// Paillier Encryption
	mpz_set_str(m, "19910813", BASE);
	mpz_set_str(seed, "1000", BASE);
	start = clock();
	PailEnc(m, n, g, n2, seed, c);
	end = clock();
	printf("%.2f\n", (double)(end - start)/1000);

// Paillier Decryption
	start = clock();
	PailDec(c, lam, n, g, n2, one, m);
	end = clock();
	printf("%.2f\n", (double)(end - start)/1000);

// Enc + Dec
	start = clock();
	PailEnc(m, n, g, n2, seed, c);
	PailDec(c, lam, n, g, n2, one, m);
	end = clock();
	printf("%.2f\n", (double)(end - start)/1000);
	
// Memory Release
	mpz_clear(p);
	mpz_clear(q);
	mpz_clear(lam);
	mpz_clear(g);
	mpz_clear(n);
	mpz_clear(n2);
	mpz_clear(m);
	mpz_clear(c);
//	mpz_clear(one); // Cannot release this memory.

	return 0;
}
