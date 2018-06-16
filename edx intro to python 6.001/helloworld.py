# -*- coding: utf-8 -*-
"""
Created on Mon Dec  4 18:50:58 2017

@author: laf.gif
"""

varA = "string"
varB = "string2"
if type(varA) == str: print ("string involved")
elif type (varB) == str: print ("string involved")
elif varA>varB: print ("bigger")
elif varA==varB: print ("equal")
elif varA<varB: print ("smaller")


cube = -28
for guess in range(abs(cube)+1):
    if guess**3 >= abs(cube):
        break
if guess**3 != abs(cube):
    print(cube, 'is not a perfect cube')
else:
        if cube<0:
            guess = -guess
            print('Cube root of ' + str(cube) + ' is ' + str(guess))
            
            
            
            
count = 0

for letter in s:

    if letter == a or letter == e or letter == i or letter == o or letter == u:
        count+=1

print ("Number of vowels: "+str(count))


s= 'babobob'

s_str = s+'aaaaa'
bobcounter = 0

charcounter = 0
char2counter = 1

for char in s:
    charcounter += 1
    char2counter += 1
    if char == 'b' and s_str[charcounter] == 'o' and s_str[char2counter] == 'b':
        bobcounter += 1
print ("Number of times bob occurs is: "+str(bobcounter))




    while s[charcounter] <= s[(charcounter+1)]:
        charcounter += 1
        lengthcounter += 1
        if charcounter > len(s):
            break
#Write a program that prints the longest substring of s in which the letters occur in alphabetical order.
# first figure out how to determine what is the (first) longest substring of alphabetical orders, then work out how to call that specific slice of substring as the output
s = 'azcbobobegghakl'
s_mod = s+'00'

#let each letter be a number 1-26, if letter+1 maps to >=letter then continue, else len(str) stopped. len(str) needs
#to be compared to another stored len(str2) which is remapped iff len str> lenstr2 so that if tied only the first
#substring is printed
#'b' >='b' True and 'a'<'0' false
charcounter = 0
slicestart = 0
lengthcounter = 1
longestslice = 0
iteration = 0

for char in s_mod:
    if char <= char+1 then add 1 to a counter, otherwise remap char to char+1 and loop

while s_mod[charcounter] <= s_mod[(charcounter+1)]:
    lengthcounter += 1
    charcounter += 1
    
    
    
#    while s_mod[charcounter] <= s_mod[(charcounter+1)]:
#        lengthcounter += 1
#        charcounter += 1
    
#    if lengthcounter > longestslice:
#        longestslice = lengthcounter
    
#    if character == '0':
#        break
    
    
    
    elif lengthcounter > longestslice:
        longestslice = lengthcounter
        #remap longestslice to current lengthcounter iff lengthcounter>longestslice
        #this works out what is length of the longest substring of alphabetical, but doesnt return the substring
    

print ("Longest substring in alphabetical order is: "+str(bigslice))




t = 'abcddazazaaaaa'

s = t + '0'

charcount = 0
stringlength = 1
longestsubstring = 0

slicestart = 0
sliceend = 0

for char in s:
    charcount += 1
    if char <= s[charcount]:
        stringlength += 1
    elif char > s[charcount]:
        if longestsubstring < stringlength:
            longestsubstring = stringlength
            stringlength = 1
            sliceend = (charcount)
            slicestart = (charcount-longestsubstring)
        else:
            stringlength = 1
    if s[charcount] == '0':
        break
    
slicestart = int(slicestart)
sliceend = int(sliceend)
    
print(str(longestsubstring))
print(s[slicestart:sliceend])



guess = 50
low = 0
high = 100

print("Please think of a number between 0 and 100!")


check = 'a'

while check != 'c':
    print("Is your secret number "+str(guess))
    check = input ("Enter 'h' to indicate the guess is too high. Enter 'l' to indicate the guess is too low. Enter 'c' to indicate I guessed correctly. ")
    if check == 'c':
        print("Game over. Your secret number was: "+str(guess))
    elif check == 'h':
        print("high")
        high = guess
        guess = int(((guess-low)/2) + low)
        
    elif check == 'l':
        print("low")
        low = guess
        guess = int(((high-guess)/2) + guess)
    else: print("Sorry, I did not understand your input.")
        



def func_a(y):
    if True:
        return a
    x=x+1
    print(a)

def f_x(y):
    
    print(x)
    
x=3
f_x(y)


def printName(firstname, lastname, reverse):
    if reverse:
        print(lastname, firstname)
    else:
        print(firstname, lastname)




def gcdIter(a, b):
    '''
    a, b: positive integers
    
    returns: a positive integer, the greatest common divisor of a & b.
    '''
    if a == b:
        return a
    if a<b:
        counter = a
    else:
        counter = b
    while True:
        if (b%counter == 0) and (a%counter == 0):
            break
        counter -= 1
        if counter == 1:
            break
    return counter


def gcdRecur(a, b):
    if b == 0:
        return a
    else:
        return gcdRecur(b, a%b)

        if (len(aStr))%2 != 0 and char > aStr[int((len(aStr))/2)]:
            return aStr[int(((len(aStr))/2)+1):]
        elif (len(aStr))%2 != 0 and char < aStr[int((len(aStr))/2)]:
            return aStr[:(int(len(aStr))/2)]
        elif (len(aStr))%2 == 0 and char > aStr[int((len(aStr))/2)]:
            return aStr[int(((len(aStr))/2)+1):]
        elif (len(aStr))%2 == 0 and char < aStr[int(((len(aStr))/2)-1)]:
            return aStr[:int(((len(aStr))/2)-1)]

def isIn(char, aStr):
    '''
    char: a single character
    aStr: an alphabetized string
    
    returns: True if char is in aStr; False otherwise
    '''
    # Your code here. check char if it is same as middle char (int function to round off), if not test if smaller or larger and check
    # in middle of top/lower half of string. if len(str) is odd then it will check all, if even then we need to check base case of 
    # s[int(high+low/2)+1] as well
    #str length 5 midpoint is 2.5 rounded down to 2 which is correct, len 4 rounds to 2 need -1
    def bisect(char, aStr):
        if char > aStr[int((len(aStr))/2)]:
            return aStr[int((len(aStr))/2):]
        else:
            return aStr[:int((len(aStr))/2)]
    
    if char == aStr[int((len(aStr))/2)] or char == aStr[int(((len(aStr))/2)-1)]:
        return True
    elif len(aStr) == 1 or len(aStr) == 2:
        return False
    else:
        return isIn(char, bisect(char,aStr))

#Write a function called polysum that takes 2 arguments, n and s. This function should sum the area and 
#square of the perimeter of the regular polygon. The function returns the sum, rounded to 4 decimal places.


def polysum(n, s):
    '''
    n,s : positive real numbers
    returns : sum of area and square of perimeter of regular polygon with (n) number of sides and (s) length of sides
    '''
    import math
    a = (0.25*n*s**2)/(math.tan((pi/n))) + (n*n*s*s)
    return round(a,4)



#Write a program to calculate the credit card balance after one year 
#if a person only pays the minimum monthly payment required by the credit card company each month.
    



    balance - the outstanding balance on the credit card

    annualInterestRate - annual interest rate as a decimal

    monthlyPaymentRate - minimum monthly payment rate as a decimal

balance = 484
annualInterestRate = 0.2
monthlyPaymentRate = 0.04

monthlyinterest = annualInterestRate/12


#upaidbalance = at start of month 1
#= balance at end of month 0

for month in range(0,12):
    monthlypayment = monthlyPaymentRate*balance
    balance -= monthlypayment
    #balance at end of month 1 ie we want this figure at end of 12 months?
    balance = (1+monthlyinterest)*balance
    #balance = at start of month 1
    balance = round(balance,2)

    
    
    print('Remaining balance: '+str(balance))

Now write a program that calculates the minimum fixed monthly payment needed in order pay off
 a credit card balance within 12 months. By a fixed monthly payment,
 we mean a single number which does not change each month, but instead is a constant amount that will be paid each month.
 
 
 min fixed = at least balance /12 if no iterest
 	      Test Case 1:
	      balance = 3329
	      annualInterestRate = 0.2

	      Result Your Code Should Generate:
	      -------------------
	      Lowest Payment: 310
 
 
    
balance = 3329
annualInterestRate = 0.2
monthlyinterest = annualInterestRate/12
    
balance_mod = balance
counter = 0
monthlypayment = 10
while balance_mod > 0:
    balance_mod -= monthlypayment
    balance_mod = (1+monthlyinterest)*balance_mod
    
    
    
    
    counter+=1
    if counter>=12 and balance_mod>0:
        counter = 0
        balance_mod = balance
        monthlypayment += 10
        
print(str(monthlypayment))
    




balance = 999999
annualInterestRate = 0.18
monthlyinterest = annualInterestRate/12
    
balance_mod = balance

minbalance = balance/12
maxbalance = ((1+annualInterestRate)*balance)/12

counter = 0
monthlypayment = ((maxbalance - minbalance)/2) + minbalance
while abs(maxbalance - minbalance) >= 0.03:
    
    monthlypayment = ((maxbalance - minbalance)/2) + minbalance
    
    balance_mod -= monthlypayment
    balance_mod = (1+monthlyinterest)*balance_mod
    
    
    
    
    counter+=1
    #monthly payment too low
    if counter==12 and balance_mod>0:
        counter = 0
        balance_mod = balance
        minbalance = monthlypayment
        
        #monthly payment too high
    if counter==12 and balance_mod<0:
        counter = 0
        balance_mod = balance
        maxbalance = monthlypayment
        
print("Lowest Payment: "+str(round(monthlypayment,2)))



def f(x):
    '''
    x is the monthly payment
    '''

def how_many(aDict):
    '''
    aDict: A dictionary, where all the values are lists.

    returns: int, how many values are in the dictionary.
    '''
    # Your Code Here
    x = []
    y = 0
    for list in aDict.values():
        if y < len(list):
            y = len(list)
        print(len(list))
    print(y)
    for key in aDict:
        if len(aDict[key]) == y:
            print(aDict[key])
            print(key)
 
 animals = { 'a': ['aardvark'], 'b': ['baboon'], 'c': ['coati']}

animals['d'] = ['donkey']
animals['d'].append('dog')
animals['d'].append('dingo')

how_many(animals)
 
 
Write a function called getSublists, which takes as parameters a list of integers named L and an integer named n.

    assume L is not empty
    assume 0 < n <= len(L)
This function returns a list of all possible sublists in L of length n without skipping elements in L

slice continual iterations within length of L of size n until reached end of list

def getSublists(L, n):
    lists = []
    for i in range(0,len(L)):
        if len(L[i:i+n])==n:
            lists.append(L[i:i+n])
        if i+n > len(L):
            break
    return lists



getSublists([10, 4, 6, 8, 3, 4, 5, 7, 7, 2], 4)

Write a Python function that returns a list of keys in aDict with the value target. .
The list of keys you return should be sorted in increasing order.
The keys and values in aDict are both integers. 
(If aDict does not contain the value target, you should return an empty list.)
This function takes in a dictionary and an integer and returns a list.

def keysWithValue(aDict, target):
    '''
    aDict: a dictionary
    target: an integer
    '''
    # Your code here  
    workingdict = aDict.copy()
    
    list = []
    #iterate over len of dict, iter(dict). keep adding to list of keys. at end sort the list before returning
    #

    #Remove and return an arbitrary (key, value) pair from the dictionary.

    #popitem() is useful to destructively iterate over a dictionary, as often used in set algorithms. 
    #If the dictionary is empty, calling popitem() raises a KeyError.
    def helper(dict):
        #returns a smaller dict if not empty, pops the value of arbitrary key value pair as tuple
        if dict != {}:
            dict.popitem()
    
    while workingdict != {}:
        tuple = workingdict.popitem()
        if tuple[1] == target:
            list.append(tuple[0])
    list.sort()
    return list


Write a recursive procedure, called laceStringsRecur(s1, s2), which also laces together two strings.
 Your procedure should not use any explicit loop mechanism, such as a for or while loop.

For this problem, you must add exactly one line of code in each of the three places where we specify to write a line of code.

s1 = abcd
s2 = efgh
out should be aebfcgdh

also check outs for s1 longer or s2 longer

def applyF_filterG(L, f, g):
    """
    Assumes L is a list of integers
    Assume functions f and g are defined for you. 
    f takes in an integer, applies a function, returns another integer 
    g takes in an integer, applies a Boolean function, 
        returns either True or False
    Mutates L such that, for each element i originally in L, L contains  
        i if g(f(i)) returns True, and no other elements
    Returns the largest element in the mutated L or -1 if the list is empty
    """
    # Your code here
    
    
    for e in L:
        if not g(f(e)):
            L.remove(e)
            L.insert(0,'a')
    
    while 'a' in L:
        L.remove('a')
    
    if L == []:
        return -1
    return max(L)

list = ['a', 'b', 'c', 'd']
try:
    a = ''
    for e in list:
        a += e
    print(a)
except:
    print('wrong')


def fancy_divide(list_of_numbers, index):
   denom = list_of_numbers[index]
   return [simple_divide(item, denom) for item in list_of_numbers]


def simple_divide(item, denom):
   return item / denom











 
 