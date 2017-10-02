#include "matrix.h"
#include <iostream>     // std::cout
#include <sstream>      // std::stringstream
#include <fstream>      // std::ifstream
#include <thread>
#include <vector>

#define PATH "data/"
#define EXTENSION ".txt"
#define NUMBER_THREADS 4
template<typename TField>
void fillMatrix(util::Matrix<TField> &matrix, std::ifstream& fileContent){
  std::string input = ""; // Input line

  //Return to begin of file to check size
  fileContent.clear(); fileContent.seekg(0, std::ios::beg);
  getline(fileContent, input);
  int n = stoi(input); // row/column value (only first number because all matrices are square)
  if (matrix.rows != n || matrix.cols != n){
    fprintf(stderr,"Matrix created has not same size as defined in file.\n Try again\n");
    exit(EXIT_FAILURE);
  }

  //Fill Matrix
  std::stringstream ss;
  TField value; //mxn temporary value
  int row = 0;
  while(getline(fileContent, input)){
    ss << input;
    for (int j = 0; j < n ; j++){
      ss >> value;
      matrix[row][j] = value;
    }
    row++;

    ss.str(std::string()); ss.clear();
  }
}

//Minimum sub-task to multiplication (specification)
template<typename TField>
void multiplicationMin(const util::Matrix<TField> _a, const util::Matrix<TField> _b, util::Matrix<TField> &c, int line) {
      for (int j = 0; j < _b.cols; ++j) {
        c[line][j] = 0;
          for (int k = 0; k < _a.cols; ++k) {
              c[line][j] = c[line][j] + _a[line][k] * _b[k][j];
          }
      }
}


template<typename TField>
util::Matrix<TField> multiplication(const util::Matrix<TField> _a, const util::Matrix<TField> _b) {
    // Check multiplication condition
    //if (_a.cols != _b.rows)
    //    throw std::logic_error("You must provide matrices mxn and nxp.");
    // Multiply
    util::Matrix<TField> prod {_a.rows, _b.cols, 0};
    std::vector<std::thread> threads;
    for (int i = 0; i < _a.rows; ++i) {
      threads.push_back(std::thread(multiplicationMin<TField>,std::ref(_a), std::ref(_b), std::ref(prod), i));
    }

    for(auto& t : threads) {
      t.detach(); //Doesn't need to wait thread finishes to continue. Otherwise, change to join()
    }
    return prod;
}

int main(int argn, char ** argc) {
    auto start = std::chrono::steady_clock::now();
    std::string dimension = ""; // dimension of matrices
    std::string path1 = PATH; // Path of first matrix
    std::string path2 = PATH; // Path of second matrix
    int nb_threads;
    if (argn != 3){
      fprintf(stderr,"Number of input incorrect");
      exit(EXIT_FAILURE);
    } else {
      if(argn > 1){
        dimension = argc[1];
        std::string path ;// endFile(dimension);
        path1 += "A"+ dimension + "x" + dimension + EXTENSION;
        path2 += "B"+ dimension + "x" + dimension + EXTENSION;
      }
      if (argn > 2){
        nb_threads = (atoi(argc[2]) > 0) ? atoi(argc[2]) : NUMBER_THREADS; //TODO: use value
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


    util::Matrix<double> matrixC = multiplication(matrixA, matrixB);

    auto end = std::chrono::steady_clock::now();
    std::chrono::duration<double, std::milli> duration = (end - start);
    std::cout << "Duration: " << duration.count() << std::endl;
}
