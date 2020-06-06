#!/usr/bin/python

## 	@package anasyn
# 	Syntactical Analyser package. 
#

import sys, argparse, re
import logging
#from tkinter import *

import analex

logger = logging.getLogger('anasyn')

DEBUG = False
LOGGING_LEVEL = logging.DEBUG
codeGenerator = []
ligne = 0 #ligne en cours d'écriture dans object_code.txt
adresse = 0 #adresse rentrées dans la table des identifiers
operation = [] #operation en cours (mult, sous, add etc)
adProgram = 0 #pour le tra initial
traProg = 0 #pour le tra initial
argcount = 0 #compteur d'arguments à réserver et pour trastat
currentOp = 0 #Sommes nous dans le programme principal (0 = oui) (1 = non)
rangeVar = "" #portée de la variable en cours de traitement, locale ou globale

class AnaSynException(Exception):
	def __init__(self, value):
		self.value = value
	def __str__(self):
                return repr(self.value)

########################################################################				 	
#### Syntactical Diagrams
########################################################################				 	

def program(lexical_analyser):
	specifProgPrinc(lexical_analyser)
	lexical_analyser.acceptKeyword("is")
	
	codeGenerator.append("debutProg(); \n")
	incrementeLigne()

	traProg = len(codeGenerator)
	codeGenerator.append("complétée plus tard \n") #ligne complétée lorsque l'on saura où démarre le programme principal
	incrementeLigne()

	corpsProgPrinc(lexical_analyser)
	

def specifProgPrinc(lexical_analyser):
	lexical_analyser.acceptKeyword("procedure")
	ident = lexical_analyser.acceptIdentifier()
	logger.debug("Name of program : "+ident)
	
def  corpsProgPrinc(lexical_analyser):
	if not lexical_analyser.isKeyword("begin"):
		logger.debug("Parsing declarations")
		partieDecla(lexical_analyser)
		logger.debug("End of declarations")
	lexical_analyser.acceptKeyword("begin")

	if not lexical_analyser.isKeyword("end"):
		logger.debug("Parsing instructions")
		suiteInstr(lexical_analyser)
		logger.debug("End of instructions")
			
	lexical_analyser.acceptKeyword("end")
	lexical_analyser.acceptFel()
	logger.debug("End of program")

	codeGenerator.append("finProg(); \n")
	incrementeLigne()
	
def partieDecla(lexical_analyser):
	global argcount #on va modifier cette variable globale
	global adresse #on va modifier cette variable globale
	if lexical_analyser.isKeyword("procedure") or lexical_analyser.isKeyword("function") :
		listeDeclaOp(lexical_analyser)
		adProgram = ligne
		codeGenerator[traProg + 1] = "tra(" + str(adProgram) + "); \n" #on rempli la ligne du tra du programme principal avec la bonne adresse
		if not lexical_analyser.isKeyword("begin"):
			listeDeclaVar(lexical_analyser)
			codeGenerator.append("reserver(" + str(argcount) + ");\n") #on réserve le bon nombre de variables
			incrementeLigne()
			adresse = 0 #on réinitialise le compteur d'adresse puisque l'on passe dans un contexte local
		currentOp = 0 #on est dans le programme principal
	else :
		listeDeclaVar(lexical_analyser)
		codeGenerator.append("reserver(" + str(argcount) + ");\n")#on réserve le bon nombre de variables
		incrementeLigne()
		adresse = 0 #on réinitialise le compteur d'adresse puisque l'on passe dans un contexte local            

def listeDeclaOp(lexical_analyser):
	declaOp(lexical_analyser)
	lexical_analyser.acceptCharacter(";")
	if lexical_analyser.isKeyword("procedure") or lexical_analyser.isKeyword("function") :
		listeDeclaOp(lexical_analyser)

def declaOp(lexical_analyser):
	if lexical_analyser.isKeyword("procedure"):
		procedure(lexical_analyser)
	if lexical_analyser.isKeyword("function"):
		fonction(lexical_analyser)

def procedure(lexical_analyser):
	lexical_analyser.acceptKeyword("procedure")
	ident = lexical_analyser.acceptIdentifier()
	global currentOp #on va modifier cette variable globale
	logger.debug("Name of procedure : "+ident)
	if currentOp == 0:#si l'on est dans le programme principale
		analex.ajouterEntreeG(ident,"procedure",ligne,"") #on ajoute cet identifiant a la table globale
	else:
		analex.ajouterEntreeG(ident,"procedure",ligne,"") #on ajoute cet identifiant a la table globale et
		analex.dicoLoc.ajouter(ident,"procedure",ligne,"")#on ajoute cet identifiant a la table locale 

	currentOp = 1 #on passe dans une fonction ou procédure annexe
	partieFormelle(lexical_analyser)

	lexical_analyser.acceptKeyword("is")
	corpsProc(lexical_analyser)

	codeGenerator.append("retourProc();\n")
	incrementeLigne()
       

def fonction(lexical_analyser): #même fonctionnement que pour procedure(lexical_analyser)
	lexical_analyser.acceptKeyword("function")
	ident = lexical_analyser.acceptIdentifier()
	logger.debug("Name of function : "+ident)
	global currentOp
	if currentOp == 0:
		analex.ajouterEntreeG(ident,"function",ligne,"")
	else:
		analex.ajouterEntreeG(ident,"function",ligne,"")
		analex.dicoLoc.ajouter(ident,"function",ligne,"")

	currentOp = 1
	partieFormelle(lexical_analyser)

	lexical_analyser.acceptKeyword("return")
	nnpType(lexical_analyser)
        
	lexical_analyser.acceptKeyword("is")
	corpsFonct(lexical_analyser)
	codeGenerator.append("retourFonct();\n")
	incrementeLigne()


def corpsProc(lexical_analyser):
	if not lexical_analyser.isKeyword("begin"):
		partieDeclaProc(lexical_analyser)
	lexical_analyser.acceptKeyword("begin")
	suiteInstr(lexical_analyser)
	lexical_analyser.acceptKeyword("end")

def corpsFonct(lexical_analyser):
	if not lexical_analyser.isKeyword("begin"):
		partieDeclaProc(lexical_analyser)
	lexical_analyser.acceptKeyword("begin")
	suiteInstrNonVide(lexical_analyser)
	lexical_analyser.acceptKeyword("end")

def partieFormelle(lexical_analyser):
	global argcount
	lexical_analyser.acceptCharacter("(")
	if not lexical_analyser.isCharacter(")"):
		listeSpecifFormelles(lexical_analyser)

	lexical_analyser.acceptCharacter(")")

def listeSpecifFormelles(lexical_analyser):
	specif(lexical_analyser)
	if not lexical_analyser.isCharacter(")"):
		lexical_analyser.acceptCharacter(";")
		listeSpecifFormelles(lexical_analyser)

def specif(lexical_analyser):
	listeIdent(lexical_analyser)
	lexical_analyser.acceptCharacter(":")
	if lexical_analyser.isKeyword("in"):
		mode(lexical_analyser)
                
	nnpType(lexical_analyser)

def mode(lexical_analyser):
	lexical_analyser.acceptKeyword("in")
	if lexical_analyser.isKeyword("out"):
		lexical_analyser.acceptKeyword("out")
		logger.debug("in out parameter")                
	else:
		logger.debug("in parameter")

def nnpType(lexical_analyser):
	if lexical_analyser.isKeyword("integer"):
		lexical_analyser.acceptKeyword("integer")
		logger.debug("integer type")
	elif lexical_analyser.isKeyword("boolean"):
		lexical_analyser.acceptKeyword("boolean")
		logger.debug("boolean type")                
	else:
		logger.error("Unknown type found <"+ lexical_analyser.get_value() +">!")
		raise AnaSynException("Unknown type found <"+ lexical_analyser.get_value() +">!")

def partieDeclaProc(lexical_analyser):
	global argcount #on va modifier ces variables globales
	global adresse
	listeDeclaVar(lexical_analyser)
	codeGenerator.append("reserver(" + str(argcount) + ");\n") #on reserve le bon nombre de variables
	incrementeLigne()
	adresse = 0 #on reinitialise les compteurs

def listeDeclaVar(lexical_analyser):
	declaVar(lexical_analyser)
	if lexical_analyser.isIdentifier():
		listeDeclaVar(lexical_analyser)

def declaVar(lexical_analyser):
	global argcount
	listeIdent(lexical_analyser)
	lexical_analyser.acceptCharacter(":")
	logger.debug("now parsing type...")
	nnpType(lexical_analyser)
	lexical_analyser.acceptCharacter(";")

def listeIdent(lexical_analyser):
	ident = lexical_analyser.acceptIdentifier()
	logger.debug("identifier found: "+str(ident))
	if currentOp == 0: #si l'on est dans le programme principal
		analex.ajouterEntreeG(ident,"identifier",adresse,"") #on ajoute l'identifiant a la table globale
	else:
		analex.ajouterEntreeG(ident,"identifier",adresse,"") #on ajoute l'identifiant a la table globale
		analex.dicoLoc.ajouter(ident,"identifier",adresse,"") #on ajoute l'identifiant a la table locale
		incrementeAdresse() #on incremente l'adresse pour la variable suivante
	if lexical_analyser.isCharacter(","):
		lexical_analyser.acceptCharacter(",")
		listeIdent(lexical_analyser)

def suiteInstrNonVide(lexical_analyser):
	instr(lexical_analyser)
	if lexical_analyser.isCharacter(";"):
		lexical_analyser.acceptCharacter(";")
		suiteInstrNonVide(lexical_analyser)

def suiteInstr(lexical_analyser):
	if not lexical_analyser.isKeyword("end"):
		suiteInstrNonVide(lexical_analyser)

def instr(lexical_analyser):
	global argcount	
	if lexical_analyser.isKeyword("while"):
		boucle(lexical_analyser)
	elif lexical_analyser.isKeyword("if"):
		altern(lexical_analyser)
	elif lexical_analyser.isKeyword("get") or lexical_analyser.isKeyword("put"):
		es(lexical_analyser)
	elif lexical_analyser.isKeyword("return"):
		retour(lexical_analyser)
	elif lexical_analyser.isIdentifier():
		ident = lexical_analyser.acceptIdentifier()
		global rangeVar
		rangeVar = analex.dicoLoc.rangeIdent(ident)
		if lexical_analyser.isSymbol(":="):				
			# affectation

			lexical_analyser.acceptSymbol(":=")
			if parametreOut(ident): #est-ce un parametre de type out 
				codeGenerator.append("empilerParam(" + str(analex.adresse(ident))+ ");\n")
				incrementeLigne()
			elif rangeVar == "local": #est-ce une variable locale 
				codeGenerator.append("empilerAd(" + str(analex.adresse(ident))+ ");\n")
				incrementeLigne()
			else : #est-ce une variable globale
				codeGenerator.append("empiler(" + str(analex.adresse(ident))+ ");\n")
				incrementeLigne()
			expression(lexical_analyser)
			logger.debug("parsed affectation")

			codeGenerator.append("affectation();\n")
			incrementeLigne()


		elif lexical_analyser.isCharacter("("): #on appelle une fonction/procédure
			lexical_analyser.acceptCharacter("(")

			codeGenerator.append("reserverBloc()\n")
			incrementeLigne()

			if not lexical_analyser.isCharacter(")"):
				listePe(lexical_analyser)

			lexical_analyser.acceptCharacter(")")
			logger.debug("parsed procedure call")
			codeGenerator.append("traStat(" + str(analex.adresse(ident)) + "," + str(argcount) + ");\n") #appel de la fonction a la bonne ligne et avec le bon nombre d'arguments
			incrementeLigne()



		else:
			logger.error("Expecting procedure call or affectation!")
			raise AnaSynException("Expecting procedure call or affectation!")
		
	else:
		logger.error("Unknown Instruction <"+ lexical_analyser.get_value() +">!")
		raise AnaSynException("Unknown Instruction <"+ lexical_analyser.get_value() +">!")

def listePe(lexical_analyser):
	expression(lexical_analyser)
	incrementeArgcount()#on a une variable/expression de plus dans notre liste d'arguments
	if lexical_analyser.isCharacter(","):
		lexical_analyser.acceptCharacter(",")
		listePe(lexical_analyser)

def expression(lexical_analyser):
	logger.debug("parsing expression: " + str(lexical_analyser.get_value()))

	exp1(lexical_analyser)
	if lexical_analyser.isKeyword("or"):
		lexical_analyser.acceptKeyword("or")
		exp1(lexical_analyser)
	writeOperation()
	global operation
	operation = [] #on est dans une opération unaire ou binaire, on initialise notre mémoire de l'opération en cours


        
def exp1(lexical_analyser):
	logger.debug("parsing exp1")
	
	exp2(lexical_analyser)
	if lexical_analyser.isKeyword("and"):
		lexical_analyser.acceptKeyword("and")
		exp2(lexical_analyser)
        
def exp2(lexical_analyser):
	logger.debug("parsing exp2")
        
	exp3(lexical_analyser)
	if	lexical_analyser.isSymbol("<") or \
		lexical_analyser.isSymbol("<=") or \
		lexical_analyser.isSymbol(">") or \
		lexical_analyser.isSymbol(">="):
		opRel(lexical_analyser)
		exp3(lexical_analyser)
	elif lexical_analyser.isSymbol("=") or \
		lexical_analyser.isSymbol("/="): 
		opRel(lexical_analyser)
		exp3(lexical_analyser)
	
def opRel(lexical_analyser):
	logger.debug("parsing relationnal operator: " + lexical_analyser.get_value())
	global operation #on va mémoriser l'operation
	if	lexical_analyser.isSymbol("<"):
		lexical_analyser.acceptSymbol("<")
		operation.append("<")
        
	elif lexical_analyser.isSymbol("<="):
		lexical_analyser.acceptSymbol("<=")
		operation.append("<=")
        
	elif lexical_analyser.isSymbol(">"):
		lexical_analyser.acceptSymbol(">")
		operation.append(">")
        
	elif lexical_analyser.isSymbol(">="):
		lexical_analyser.acceptSymbol(">=")
		operation.append(">=")
        
	elif lexical_analyser.isSymbol("="):
		lexical_analyser.acceptSymbol("=")
		operation.append("=")
        
	elif lexical_analyser.isSymbol("/="):
		lexical_analyser.acceptSymbol("/=")
		operation.append("/=")
        
	else:
		msg = "Unknown relationnal operator <"+ lexical_analyser.get_value() +">!"
		logger.error(msg)
		raise AnaSynException(msg)

def exp3(lexical_analyser):
	logger.debug("parsing exp3")
	exp4(lexical_analyser)	
	if lexical_analyser.isCharacter("+") or lexical_analyser.isCharacter("-"):
		opAdd(lexical_analyser)
		exp4(lexical_analyser)

def opAdd(lexical_analyser):
	logger.debug("parsing additive operator: " + lexical_analyser.get_value())
	global operation #on va mémoriser l'opération
	if lexical_analyser.isCharacter("+"):
		operation.append("+")
		lexical_analyser.acceptCharacter("+")
                
	elif lexical_analyser.isCharacter("-"):
		operation.append("-")
		lexical_analyser.acceptCharacter("-")
                
	else:
		msg = "Unknown additive operator <"+ lexical_analyser.get_value() +">!"
		logger.error(msg)
		raise AnaSynException(msg)

def exp4(lexical_analyser):
	logger.debug("parsing exp4")
        
	prim(lexical_analyser)	
	if lexical_analyser.isCharacter("*") or lexical_analyser.isCharacter("/"):

		opMult(lexical_analyser)
		prim(lexical_analyser)

def opMult(lexical_analyser):
	logger.debug("parsing multiplicative operator: " + lexical_analyser.get_value())
	global operation #on va mémoriser l'opération
	if lexical_analyser.isCharacter("*"):
		operation.append("*")
		lexical_analyser.acceptCharacter("*")
                
	elif lexical_analyser.isCharacter("/"):
		operation.append("/")
		lexical_analyser.acceptCharacter("/")
                
	else:
		msg = "Unknown multiplicative operator <"+ lexical_analyser.get_value() +">!"
		logger.error(msg)
		raise AnaSynException(msg)

def prim(lexical_analyser):
	logger.debug("parsing prim")
        
	if lexical_analyser.isCharacter("+") or lexical_analyser.isCharacter("-") or lexical_analyser.isKeyword("not"):

		opUnaire(lexical_analyser)
	elemPrim(lexical_analyser)

def opUnaire(lexical_analyser):
	logger.debug("parsing unary operator: " + lexical_analyser.get_value())
	global operation #on va mémoriser l'opération
	if lexical_analyser.isCharacter("+"):
		lexical_analyser.acceptCharacter("+")
                
	elif lexical_analyser.isCharacter("-"):
		operation.append("moins")
		lexical_analyser.acceptCharacter("-")
                
	elif lexical_analyser.isKeyword("not"):
		operation.append("not")
		lexical_analyser.acceptKeyword("not")
                
	else:
		msg = "Unknown additive operator <"+ lexical_analyser.get_value() +">!"
		logger.error(msg)
		raise AnaSynException(msg)

def elemPrim(lexical_analyser):
	logger.debug("parsing elemPrim: " + str(lexical_analyser.get_value()))
	if lexical_analyser.isCharacter("("):
		lexical_analyser.acceptCharacter("(")
		expression(lexical_analyser)
		lexical_analyser.acceptCharacter(")")
	elif lexical_analyser.isInteger() or lexical_analyser.isKeyword("true") or lexical_analyser.isKeyword("false"):
		valeur(lexical_analyser)

	elif lexical_analyser.isIdentifier():
		ident = lexical_analyser.acceptIdentifier()
		global rangeVar
		global argcount
		rangeVar = analex.dicoLoc.rangeIdent(ident) #on mémorise la portée de l'identifiant
		if lexical_analyser.isCharacter("("): #c'est un appel de fonction/procédure
			argcount = 0 #on initialise notre compteur d'arguments
			codeGenerator.append("reserverBloc();\n")
			incrementeLigne()
			lexical_analyser.acceptCharacter("(")
			if not lexical_analyser.isCharacter(")"):
				listePe(lexical_analyser)

			lexical_analyser.acceptCharacter(")")


			logger.debug("parsed procedure call")
			codeGenerator.append("traStat(" + str(analex.adresse(ident)) + "," + str(argcount) + ");\n")
			incrementeLigne()
			
			logger.debug("Call to function: " + ident)
		else:
			logger.debug("Use of an identifier as an expression: " + ident) #c'est un identifiant qui est utilisé comme expression
			if rangeVar == "local": #quelle est la portée de la variable 
				if parametreOut(ident):
					codeGenerator.append("empilerParam(" + str(analex.adresse(ident))+ ");\n")
					incrementeLigne()
				else:
					codeGenerator.append("empilerAd(" + str(analex.adresse(ident))+ ");\n")
					incrementeLigne()
			else:
				codeGenerator.append("empiler(" + str(analex.adresse(ident))+ ");\n")
				incrementeLigne()
			codeGenerator.append("valeurPile();\n") #c'est une expression, on a alors besoin de suivre notre empiler avec valeurPile
			incrementeLigne()
	else:
		logger.error("Unknown Value!")
		raise AnaSynException("Unknown Value!")

def valeur(lexical_analyser):
	if lexical_analyser.isInteger():
		entier = lexical_analyser.acceptInteger()

		codeGenerator.append("empiler("+str(entier)+"); \n") #on empile directement l'entier
		incrementeLigne()

		logger.debug("integer value: " + str(entier))
		return "integer"
	elif lexical_analyser.isKeyword("true") or lexical_analyser.isKeyword("false"):
		vtype = valBool(lexical_analyser)
		return vtype
	else:
		logger.error("Unknown Value! Expecting an integer or a boolean value!")
		raise AnaSynException("Unknown Value ! Expecting an integer or a boolean value!")

def valBool(lexical_analyser):
	if lexical_analyser.isKeyword("true"): #on va empiler 1 ou 0 selon la valeur du bool
		lexical_analyser.acceptKeyword("true")

		codeGenerator.append("empiler(1); \n")
		incrementeLigne()
	
		logger.debug("boolean true value")
                
	else:
		logger.debug("boolean false value")

		codeGenerator.append("empiler(0); \n")
		incrementeLigne()

		lexical_analyser.acceptKeyword("false")	
        
	return "boolean"

def es(lexical_analyser):
	logger.debug("parsing E/S instruction: " + lexical_analyser.get_value())
	if lexical_analyser.isKeyword("get"):
		lexical_analyser.acceptKeyword("get")
		lexical_analyser.acceptCharacter("(")
		ident = lexical_analyser.acceptIdentifier()
		lexical_analyser.acceptCharacter(")")

		codeGenerator.append("get(); \n")
		incrementeLigne()

		logger.debug("Call to get "+ident)
	elif lexical_analyser.isKeyword("put"):
		lexical_analyser.acceptKeyword("put")
		lexical_analyser.acceptCharacter("(")
		expression(lexical_analyser)
		lexical_analyser.acceptCharacter(")")

		codeGenerator.append("put(); \n")
		incrementeLigne()

		logger.debug("Call to put")
	else:
		logger.error("Unknown E/S instruction!")
		raise AnaSynException("Unknown E/S instruction!")

def boucle(lexical_analyser):
	logger.debug("parsing while loop: ")
	lexical_analyser.acceptKeyword("while")
	ad1 = ligne #on devra se référer a cette ligne dans notre tra
	expression(lexical_analyser)

	tze = len(codeGenerator) #on stocke la ligne a laquelle on devra revenir
	codeGenerator.append("complétée plus tard \n")#on complètera lorsque l'on saura quelle est la ligne a référer
	incrementeLigne()

	lexical_analyser.acceptKeyword("loop")


	suiteInstr(lexical_analyser)

	codeGenerator.append("tra(" + str(ad1) + "); \n")
	incrementeLigne()
	lexical_analyser.acceptKeyword("end")
	ad2 = ligne #on devra se référer a cette ligne dans notre tze
	codeGenerator[tze] = "tze(" + str(ad2) + "); \n" #on remplit la ligne laissé vide
	logger.debug("end of while loop ")

def altern(lexical_analyser):
	logger.debug("parsing if: ")
	lexical_analyser.acceptKeyword("if")

	expression(lexical_analyser)

	tze = len(codeGenerator) #on stocke la ligne a laquelle on devra revenir
	codeGenerator.append("complétée plus tard \n")#on complètera lorsque l'on saura quelle est la ligne a référer
	incrementeLigne()
	lexical_analyser.acceptKeyword("then")


	suiteInstr(lexical_analyser)

	ad1 = ligne#on devra se référer a cette ligne dans notre tze
	codeGenerator[tze] = "tze("+ str(ad1) + "); \n"
	
	if lexical_analyser.isKeyword("else"): #c'est une alternative, on suit la même marche que pour les tra et tze précédents
		tra = len(codeGenerator)
		codeGenerator.append("complétée plus tard \n")
		incrementeLigne()
		lexical_analyser.acceptKeyword("else")
		
		suiteInstr(lexical_analyser)
       
	lexical_analyser.acceptKeyword("end")
	ad2 = ligne
	codeGenerator[tra] = "tra(" + str(ad2) + "); \n"
	logger.debug("end of if")

def retour(lexical_analyser):
	logger.debug("parsing return instruction")
	lexical_analyser.acceptKeyword("return")
	expression(lexical_analyser)

################################################################################

def incrementeLigne():
	global ligne
	ligne = ligne + 1

def incrementeArgcount():
	global argcount
	argcount = argcount + 1

def incrementeAdresse():
	global adresse
	adresse = adresse + 1

def writeOperation(): #on écrit dans le code objet l'opération en mémoire
	while operation != [] :
		if operation[-1] == "=":
			codeGenerator.append("egal();\n")
			incrementeLigne()
			operation.pop(-1)
		elif operation[-1] == "moins":
			codeGenerator.append("moins();\n")
			incrementeLigne()
			operation.pop(-1)
		elif operation[-1] == "not":
			codeGenerator.append("non();\n")
			incrementeLigne()
			operation.pop(-1)
		elif operation[-1] == "+":
			codeGenerator.append("add();\n")
			incrementeLigne()
			operation.pop(-1)
		elif operation[-1] == "-":
			codeGenerator.append("sous();\n")
			incrementeLigne()
			operation.pop(-1)
		elif operation[-1] == "*":
			codeGenerator.append("mult();\n")
			incrementeLigne()
			operation.pop(-1)
		elif operation[-1] == "/":
			codeGenerator.append("div();\n")
			incrementeLigne()
			operation.pop(-1)
		elif operation[-1] == "/=":
			codeGenerator.append("diff();\n")
			incrementeLigne()
			operation.pop(-1)
		elif operation[-1] == "<":
			codeGenerator.append("inf();\n")
			incrementeLigne()
			operation.pop(-1)
		elif operation[-1] == "<=":
			codeGenerator.append("infeq();\n")
			incrementeLigne()
			operation.pop(-1)
		elif operation[-1] == ">":
			codeGenerator.append("sup();\n")
			incrementeLigne()
			operation.pop(-1)
		elif operation[-1] == ">=":
			codeGenerator.append("supeq();\n")
			incrementeLigne()
			operation.pop(-1)

def parametreOut(ident):
	if analex.dicoLoc.trouver(ident):
		return True


########################################################################				 	
def main():

	parser = argparse.ArgumentParser(description='Do the syntactical analysis of a NNP program.')
	parser.add_argument('inputfile', type=str, nargs=1, help='name of the input source file')
	parser.add_argument('-o', '--outputfile', dest='outputfile', action='store', \
		default="", help='name of the output file (default: stdout)')
	parser.add_argument('-v', '--version', action='version', version='%(prog)s 1.0')
	parser.add_argument('-d', '--debug', action='store_const', const=logging.DEBUG, \
		default=logging.INFO, help='show debugging info on output')
	parser.add_argument('-p', '--pseudo-code', action='store_const', const=True, default=False, \
		help='enables output of pseudo-code instead of assembly code')
	parser.add_argument('--show-ident-table', action='store_true', \
		help='shows the final identifiers table')
	args = parser.parse_args()

	filename = args.inputfile[0]
	f = None
	try:
		f = open(filename, 'r', encoding="utf8", errors='ignore')
	except:
		print("Error: can\'t open input file!")
		return
		
	outputFilename = args.outputfile
	
  	# create logger      
	LOGGING_LEVEL = args.debug
	logger.setLevel(LOGGING_LEVEL)
	ch = logging.StreamHandler()
	ch.setLevel(LOGGING_LEVEL)
	formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
	ch.setFormatter(formatter)
	logger.addHandler(ch)

	if args.pseudo_code:
		True#
	else:
		False#
	"""
	root = Tk()
	root.title("Compilateur")

	left = Frame(root, borderwidth=2, relief="solid")
	right = Frame(root, borderwidth=2, relief="solid")
	container1 = Frame(left)
	container2 = Frame(right)

	labelLeft = Label(container1, text="Programme compilé :")
	scroll = Scrollbar(container1)
	scroll.pack(side=RIGHT, fill=Y)
	filecontent = Text(container1, yscrollcommand=scroll.set)
	with open(args.inputfile[0], 'rb') as f:
		filecontent.insert(INSERT, f.read())
	labelRight = Label(container2, text="Résultat de la compilation :")

	left.pack(side="left", expand=True, fill="both")
	right.pack(side="right", expand=True, fill="both")
	container1.pack(expand=True, fill="both", padx=5, pady=5)
	container2.pack(expand=True, fill="both", padx=5, pady=5)

	labelLeft.pack()
	labelRight.pack()
	filecontent.pack()

	root.mainloop()"""

	lexical_analyser = analex.LexicalAnalyser()
	
	lineIndex = 0
	for line in f:
		line = line.rstrip('\r\n')
		lexical_analyser.analyse_line(lineIndex, line)
		lineIndex = lineIndex + 1
	f.close()
	

	# launch the analysis of the program
	lexical_analyser.init_analyser()
	program(lexical_analyser)
		
	if args.show_ident_table:
			print("------ IDENTIFIER TABLE ------")
			#print(str(identifierTable))
			print("------ END OF IDENTIFIER TABLE ------")


	if outputFilename != "":
			try:
					output_file = open(outputFilename, 'w')
			except:
					print("Error: can\'t open output file!")
					return
	else:
			output_file = sys.stdout

	# Outputs the generated code to a file
	output_file = open("../../codeObjets/object_code.txt", 'w')
	instrIndex = 0
	while instrIndex < len(codeGenerator): #on rempli object_code.txt avec les chaînes de caractères en mises en mémoire durant l'execution du programme
		output_file.write(codeGenerator[instrIndex])
		instrIndex += 1

	print(analex.identifierTableGlobale)

	if outputFilename != "":
			output_file.close() 

########################################################################				 

if __name__ == "__main__":
    main() 

