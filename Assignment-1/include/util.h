#ifndef _UTIL_
#define _UTIL_
#include "matrix.h"
#include <fstream>      // std::ifstream

/**
 * Fill Matrice @matrix with content file
 * Check first number in @filecontent to match with size of @matrix
 * TODO: Check second number and allow matrices not square.
 * */
template<typename TField>
void fillMatrix(util::Matrix<TField> &matrix, std::ifstream& fileContent);

#include "../src/util.inl"
#endif
