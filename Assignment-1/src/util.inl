#include "util.h"
#include "matrix.h"
#include <fstream>      // std::ifstream
#include <sstream>      // std::stringstream

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
    if(n <= row) throw std::logic_error("Matrix has more lines than defined in the begin of file.\n");
    ss << input;
    for (int j = 0; j < n ; j++){
      ss >> value;
      matrix[row][j] = value;
    }
    row++;

    ss.str(std::string()); ss.clear();
  }
}

void writeMatrix(util::Matrix<double> matrixC){
  std::string output_path = "output/C"+ std::to_string(matrixC.rows) + "x" + std::to_string(matrixC.cols) + EXTENSION;
  std::ofstream outFile(output_path, std::fstream::out | std::ios::trunc);
  if(!outFile){
    std::cerr << "vixe, problem with " << output_path << std::endl;
  }
  outFile << matrixC;
  outFile.close();
}
