# Key-Parirty-based-File-Encryptor

This project proposes a centralized file system which is secured through password encryption and File encryption. The users store their files in a remote file server and can retrieve the files whenever required. The passwords are salted and stored in order to secure them. Files are encrypted in the bit-level and use a 64-bit key which is unique to each user. The key is represented as an 8x8 matrix and the parity of each row is considered for transforming the blocks of the file bits. The File encryption is done in a multi-threaded fashion in order to speedup the encryption process.

C. File Encryption
The file encryption is performed after dividing the bits of

the file into 64-bit blocks. The key as well as each block is
represented as an 8x8 matrix. 

The following steps are followed

Step 1: Bits of the file are extracted

Step 2: The bits are divided into 64-bit blocks represented as
an 8x8 matrix

Step 3: The key is represented as an 8x8 matrix and parity
value of each row is obtained and stored. That is, if
the number of ones in the row is even, the parity value
is 0 and if the number is odd, the parity value is 1.

Step 4: A fixed N number of threads are created for encryption
and the blocks, the key along with the parity
information is provided to each encryption module.

Step 5: The blocks are distributed among the threads uniformly
and follow the following expression.

Step 6: The encryption algorithm applied on each block in
each thread is as follows
a) Perform matrix transpose for the block.
b) For each row of the plain text block, if the parity
value is 1 perform a left circular shift.
c) Perform XOR operation with the key and the block.

Step 7: Collect all the blocks from the threads after their
encryption and create a linear bit sequence.

Step 8: This linear bit sequence obtained is the cipher, and is
written to a file which is the encrypted file.
