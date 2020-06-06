




Machine Virtuelle:

Afin de compiler le code objet du language que l'on a passé dans l'analyseur syntaxique, il suffit de le placer dans
le dossier codeObjets et dans le mainMachine il faut remplacer le texte1.txt par le nomFichier.txt et ensuite lancer
le main pour la compilation.
Si vous voulez voir les valeurs de la pile vous pouvez enlever les /* pour les commentaire debbuger aux
lignes 347 et 515

Analyseur syntaxique: 

Afin de lancer l'analyseur syntaxique il suffit de rentrer dans un terminal, dans le dossier Analyseur :
python3 anasyn.py /path/fichier.nno avec fichier.nno le code à compiler avec le path correct, on utilisera pour les codes du dossier tests :
./../../tests/nnp/correctX.nno avec X = 1 2 3 4 ou 5 
Le code objet est généré directement dans le dossier codeObjets sous le nom object_code.txt

La table des identifiants est directement affichée dans le terminal une fois l'analyseur syntaxique compilé.
