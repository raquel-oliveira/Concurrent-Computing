#include "matrix.h"
#include <iostream>     // std::cout
#include <fstream>      // std::ifstream
#include "util.h"       // fillMatrix

int main(int argn, char ** argc) {
    auto start = std::chrono::steady_clock::now();
    std::string dimension = ""; // dimension of matrices
    std::string path1 = PATH; // Path of first matrix
    std::string path2 = PATH; // Path of second matrix
    if (argn != 2){
      fprintf(stderr,"Number of input incorrect");
      exit(EXIT_FAILURE);
    } else {
      if(argn > 1){
        dimension = argc[1];
        std::string path ;// endFile(dimension);
        path1 += "A"+ dimension + "x" + dimension + EXTENSION;
        path2 += "B"+ dimension + "x" + dimension + EXTENSION;
      }
    }

    //Check if matrices exists
    std::ifstream matrixA_txt(path1);
    std::ifstream matrixB_txt(path2);
    if (matrixA_txt.fail() || matrixB_txt.fail()){
      fprintf(stderr,"Error trying to open file(matrix) \n Try again\n");
      exit(EXIT_FAILURE);
    }

    //Read First Matrix
    std::string input = ""; // Input line
    getline(matrixA_txt, input);
    int n = stoi(input); //only first number because all matrices are square
    util::Matrix<double> matrixA{n};
    fillMatrix(matrixA, matrixA_txt);

    //Read Second Matrix
    getline(matrixB_txt, input);
    n = stoi(input);
    util::Matrix<double> matrixB{n};
    fillMatrix(matrixB, matrixB_txt);

    util::Matrix<double> matrixC = matrixA * matrixB;
    writeMatrix(matrixC);
    auto end = std::chrono::steady_clock::now();
    std::chrono::duration<double, std::milli> duration = (end - start);
    std::cout << duration.count() << std::endl;
}
