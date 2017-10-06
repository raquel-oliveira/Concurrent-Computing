#ifndef _UTIL_
#define _UTIL_
#include "matrix.h"
#include <fstream>      // std::ifstream

#define PATH "data/"
#define EXTENSION ".txt"

/**
 * Fill Matrice @matrix with content file
 * Check first number in @filecontent to match with size of @matrix
 * TODO: Check second number and allow matrices not square.
 * */
template<typename TField>
void fillMatrix(util::Matrix<TField> &matrix, std::ifstream& fileContent);

/**
  * Create a file with the format: Crowxcol.EXTENSION with data of @param matrixC
  * where the 'row' is the dimension of @param matrixC
  *       the 'col' is the dimension of cols of @param matrixC
  * @param matrixC     matrix(data) of output file
  * */
void writeMatrix(util::Matrix<double> matrixC);

#include "../src/util.inl"
#endif
