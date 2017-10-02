# Makefile
#
# Makefile completo separando os diferentes elementos da aplicacao
# como codigo (src), cabecalhos (include), executaveis (build),
# bibliotecas (lib), etc. Cada elemento e alocado em uma pasta
# especifica, organizando melhor o codigo fonte.
#
# Algumas variaveis sao usadas com significado especial:
#
# $@ nome do alvo (target)
# $^ lista com os nomes de todos os pre-requisitos sem duplicatas
# $< nome do primeiro pre-requisito
#

# Comandos do sistema operacional
# Linux: rm -rf
# Windows: cmd //C del
RM = rm -rf

# Compilador
CC=g++

# Variaveis para os subdiretorios
INC_DIR=include
SRC_DIR=src
OBJ_DIR=build
#BIN_DIR=bin
DOC_DIR=doc

# Opcoes de compilacao
CFLAGS=-Wall -pedantic -std=c++11 -I. -I$(INC_DIR)

# Garante que os alvos desta lista nao sejam confundidos com arquivos
# de mesmo nome
.PHONY: all init clean debug doxy doc

# Define o alvo (target) para a compilacao completa e os alvos
# de dependencia. Ao final da compilacao, remove os arquivos objeto.
all: init multimat_sequencial

debug: CFLAGS += -g -O0
debug: all

# Alvo (target) para a criação da estrutura de diretorios
# necessaria para a geracao dos arquivos objeto
init:
	@mkdir -p $(BIN_DIR)/
	@mkdir -p $(OBJ_DIR)/

# Alvo (target) para a construcao do executavel create1
# Define o arquivo build/noThread.o como dependencia
multimat_sequencial: $(OBJ_DIR)/sequencial.o
	$(CC) $(CFLAGS) -o multimat_sequencial $^
	@echo "Executavel ./multimat_sequencial criado\n"

# Define o arquivo src/main.cpp como dependencia
$(OBJ_DIR)/sequencial.o: $(SRC_DIR)/multimat_sequencial.cpp $(INC_DIR)/matrix.h
	$(CC) -c $(CFLAGS) -o $@ $<

# Alvo (target) para a geração automatica de documentacao
# usando o Doxygen. Sempre remove a documentacao anterior (caso exista)
# e gera uma nova.
doxy:
	doxygen -g

doc:
	$(RM) $(DOC_DIR)/*;mkdir -p $(DOC_DIR)/
	doxygen

# Alvo (target) usado para limpar os arquivos temporarios (objeto)
# gerados durante a compilacao, assim como os arquivos
# binarios/executaveis.
clean:
	#$(RM) $(BIN_DIR)/*
	$(RM) $(OBJ_DIR)/*
