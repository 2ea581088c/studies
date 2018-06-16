# Hangman game
#

# -----------------------------------
# Helper code
# You don't need to understand this helper code,
# but you will have to know how to use the functions
# (so be sure to read the docstrings!)

import random

WORDLIST_FILENAME = "words.txt"

def loadWords():
    """
    Returns a list of valid words. Words are strings of lowercase letters.
    
    Depending on the size of the word list, this function may
    take a while to finish.
    """
    print("Loading word list from file...")
    # inFile: file
    inFile = open(WORDLIST_FILENAME, 'r')
    # line: string
    line = inFile.readline()
    # wordlist: list of strings
    wordlist = line.split()
    print("  ", len(wordlist), "words loaded.")
    return wordlist

def chooseWord(wordlist):
    """
    wordlist (list): list of words (strings)

    Returns a word from wordlist at random
    """
    return random.choice(wordlist)

# end of helper code
# -----------------------------------

# Load the list of words into the variable wordlist
# so that it can be accessed from anywhere in the program
wordlist = loadWords()

def isWordGuessed(secretWord, lettersGuessed):
    '''
    secretWord: string, the word the user is guessing
    lettersGuessed: list, what letters have been guessed so far
    returns: boolean, True if all the letters of secretWord are in lettersGuessed;
      False otherwise
    '''
    # FILL IN YOUR CODE HERE...
    listWord = list(secretWord)
    #listword = list of all letters in word each letter is its own element
    # i have to remove duplicate letters from listword. or i can apply lettersguessed to remove elements from listword and check
    #if i have an empty list for listword to return True/False
    for char in lettersGuessed:
        while (char in lettersGuessed) and (char in listWord):
            listWord.remove(char)
    return ((listWord) == [])


def getGuessedWord(secretWord, lettersGuessed):
    '''
    secretWord: string, the word the user is guessing
    lettersGuessed: list, what letters have been guessed so far
    returns: string, comprised of letters and underscores that represents
      what letters in secretWord have been guessed so far.
    '''
    # FILL IN YOUR CODE HERE...
    
    #iterate over the listWord until all guessed letters removed. we need to maintain the ordering of listWord so replace all
    # guessed letter with a '0'.
    #this means listWord becomes only the unguessed letters+'0' ie a 0 0 l 0. we need to compare listWord to original list
    #of word and replace all 0 with actual index, and all char with '_ ' ie replace all unguessed letter with '_ '
    #then finally convert list to a string and print.
    listWord = list(secretWord)
    #listword = list of all letters in word each letter is its own element
    # i have to remove duplicate letters from listword. or i can apply lettersguessed to remove elements from listword and check
    #if i have an empty list for listword to return True/False
    for char in lettersGuessed:
        while (char in lettersGuessed) and (char in listWord):
            listWord.remove(char)
    #this gives listword as list of unguessed letters in word but with repeats. but same number of repeats as in secretword
    listword2 = list(secretWord)
    for i in range(0, len(listword2)):
        #i starts from 0 to whatever final index so for every char in listWord we will remove a char from 
        #the only way to replace is by removing, then inserting at the correct index on listword2
        if listword2[i] in listWord:
            listword2.remove(listword2[i])
            listword2.insert(i, '_ ')
            #this results in listword2 becoming a list of all chars in the str to be returned
    strword = ''
    for char in listword2:
        strword = strword + char
    
    
    return strword

def getAvailableLetters(lettersGuessed):
    '''
    lettersGuessed: list, what letters have been guessed so far
    returns: string, comprised of letters that represents what letters have not
      yet been guessed.
    '''
    # FILL IN YOUR CODE HERE...

    import string
    answer = ''
    for i in range(0, len(string.ascii_lowercase)):
        if string.ascii_lowercase[i] not in lettersGuessed:
            answer = answer + string.ascii_lowercase[i]
            

    return answer
    
secretWord = chooseWord(wordlist)

def hangman(secretWord):
    '''
    secretWord: string, the secret word to guess.

    Starts up an interactive game of Hangman.

    * At the start of the game, let the user know how many 
      letters the secretWord contains.

    * Ask the user to supply one guess (i.e. letter) per round.

    * The user should receive feedback immediately after each guess 
      about whether their guess appears in the computers word.

    * After each round, you should also display to the user the 
      partially guessed word so far, as well as letters that the 
      user has not yet guessed.

    Follows the other limitations detailed in the problem write-up.
    '''
    # FILL IN YOUR CODE HERE...
    

    #let user know how many letters secretword has
    print("Welcome to the game, Hangman!")
    print("I am thinking of a word that is "+str(len(secretWord))+" letters long")
    print('-----------')
    
    guessesleft = 8
    lettersGuessed = []
    
    while guessesleft>0:
    
        print('You have ' + str(guessesleft) + ' guesses left.')
        print('Available letters: ' + getAvailableLetters(lettersGuessed))
    
        #input gets put in every round
        guess = input('Please guess a letter: ')
    

        # scenarios : if guess is already guessed, if guess is not guessed but correct, if guess is not guessed and
        # incorrect
        if guess.lower() not in lettersGuessed:
            lettersGuessed.append(guess.lower())
            
            
            
            
            if guess.lower() in secretWord:
                print('Good guess: ' + getGuessedWord(secretWord, lettersGuessed))
                print('-----------')
                
                if isWordGuessed(secretWord, lettersGuessed):
                    print('Congratulations, you won!')
                    break
            else:
                guessesleft -= 1
                print('Oops! That letter is not in my word: '+getGuessedWord(secretWord, lettersGuessed))
                print('-----------')
                if guessesleft == 0:
                    print('Sorry, you ran out of guesses. The word was ' + str(secretWord)+'.')
        
        else :
            print("Oops! You've already guessed that letter: "+getGuessedWord(secretWord, lettersGuessed))
            print('-----------')
            
    
   
    
    #change guess to lowercase
    
    






# When you've completed your hangman function, uncomment these two lines
# and run this file to test! (hint: you might want to pick your own
# secretWord while you're testing)

# secretWord = chooseWord(wordlist).lower()
# hangman(secretWord)
