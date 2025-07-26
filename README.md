# 🧵 Multithreaded Matrix Multiplication in C and Java using WSL2

This guide presents the implementation of **multithreaded matrix multiplication** using both **Pthreads in C** and **Java Threads**, executed inside a Linux environment on Windows using **WSL2**.

---

## 🖥️ System Environment

- **OS**: Windows 11 Pro (Version 24H2)
- **Linux**: Ubuntu 24.04.2 LTS via WSL2
- **C Compiler**: GCC 13.4
- **Java Compiler**: OpenJDK 17.0.15
- **Text Editor**: `nano` (inside Ubuntu)
- **Code Editor**: Visual Studio Code (v1.101)

---

## 🎯 Objective

To perform matrix multiplication in parallel by:
- Creating multiple threads using **Pthreads (C)** and **Java Threads (Java)**
- Assigning each thread the task of computing a row of the result matrix
- Comparing thread usage and management in both languages

---

## 📂 Assumptions Made

- Matrices are square and of fixed size: **3 × 3**
- One thread per matrix row
- Values are hardcoded (not entered by user)

---

## ⚙️ Step 1: WSL2 and Ubuntu Setup

> Same as in the [Fibonacci.md](#) guide, briefly:

1. **Enable WSL & Virtual Machine Platform** in Windows Features  
2. Open PowerShell and run:
   ```bash
   wsl --install
   ```
3. Create a Linux user → Launch Ubuntu from Start Menu  
4. Update system & install build tools:
   ```bash
   sudo apt update && sudo apt upgrade
   sudo apt install build-essential gcc openjdk-17-jdk
   ```

---

## 🧾 Step 2: Write C Program Using Pthreads

1. **Create C file**
   ```bash
   nano MatrixMultiplication.c
   ```

2. **Paste C Code**
   ```c
   #include <stdio.h>
   #include <pthread.h>

   #define SIZE 3

   int A[SIZE][SIZE] = {
       {1, 2, 3},
       {4, 5, 6},
       {7, 8, 9}
   };

   int B[SIZE][SIZE] = {
       {7, 8, 9},
       {4, 5, 6},
       {1, 2, 3}
   };

   int result[SIZE][SIZE];

   void* compute_row(void* arg) {
       int row = *(int*)arg;
       for (int col = 0; col < SIZE; col++) {
           result[row][col] = 0;
           for (int k = 0; k < SIZE; k++) {
               result[row][col] += A[row][k] * B[k][col];
           }
       }
       pthread_exit(NULL);
   }

   int main() {
       pthread_t threads[SIZE];
       int row_ids[SIZE];

       for (int i = 0; i < SIZE; i++) {
           row_ids[i] = i;
           pthread_create(&threads[i], NULL, compute_row, &row_ids[i]);
       }

       for (int i = 0; i < SIZE; i++) {
           pthread_join(threads[i], NULL);
       }

       printf("Resultant Matrix:\n");
       for (int i = 0; i < SIZE; i++) {
           for (int j = 0; j < SIZE; j++) {
               printf("%d\t", result[i][j]);
           }
           printf("\n");
       }

       return 0;
   }
   ```

3. **Save and exit**: `Ctrl + O` → `Enter`, then `Ctrl + X`

4. **Compile and run**
   ```bash
   gcc MatrixMultiplication.c -o matrixc -lpthread
   ./matrixc
   ```

---

## ☕ Step 3: Write Java Program Using Threads

1. **Create Java file**
   ```bash
   nano MatrixMultiplication.java
   ```

2. **Paste Java Code**
   ```java
   public class MatrixMultiplication extends Thread {
       private int row;
       private static final int SIZE = 3;

       static int[][] A = {
           {1, 2, 3},
           {4, 5, 6},
           {7, 8, 9}
       };

       static int[][] B = {
           {7, 8, 9},
           {4, 5, 6},
           {1, 2, 3}
       };

       static int[][] result = new int[SIZE][SIZE];

       MatrixMultiplication(int row) {
           this.row = row;
       }

       public void run() {
           for (int col = 0; col < SIZE; col++) {
               result[row][col] = 0;
               for (int k = 0; k < SIZE; k++) {
                   result[row][col] += A[row][k] * B[k][col];
               }
           }
       }

       public static void main(String[] args) {
           MatrixMultiplication[] threads = new MatrixMultiplication[SIZE];

           for (int i = 0; i < SIZE; i++) {
               threads[i] = new MatrixMultiplication(i);
               threads[i].start();
           }

           for (int i = 0; i < SIZE; i++) {
               try {
                   threads[i].join();
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }

           System.out.println("Resultant Matrix:");
           for (int i = 0; i < SIZE; i++) {
               for (int j = 0; j < SIZE; j++) {
                   System.out.print(result[i][j] + "\t");
               }
               System.out.println();
           }
       }
   }
   ```

3. **Compile and run**
   ```bash
   javac MatrixMultiplication.java
   java MatrixMultiplication
   ```

---

## 🧪 Output

Both programs compute the following **3 × 3** matrix product:

```
18	24	30
54	69	84
90	114	138
```

Each row is calculated by an individual thread.

---

## 🔍 Comparison: C (Pthreads) vs Java (Threads)

| Feature               | Pthreads (C)                        | Java Threads                        |
|----------------------|-------------------------------------|-------------------------------------|
| Abstraction Level    | Low-level                           | High-level                          |
| Thread Creation      | `pthread_create()`                  | `new Thread().start()`              |
| Synchronization      | Manual (`pthread_join`)             | Built-in (`join`)                   |
| Performance Control  | Fine-grained                        | Abstracted                          |
| Portability          | Platform-specific                   | Platform-independent (Java JVM)     |
| Code Simplicity      | More boilerplate                    | Simpler to write and read           |

---

## 📁 Repository Structure

```
.
├── MatrixMultiplication.c
├── MatrixMultiplication.java
└── README.md
```

---
