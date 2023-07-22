'''
Created on 23 Oct 2018

@author: max
'''

def main():
    mode = input("Select Demo-Mode: 0-Single-Class / 1-Multi-Class")
    print(mode)
    if mode == 0:
        single_class_input()
    elif mode == 1:
        multi_class_input()
    else:
        print("Invalid input")
        exit()
    
def single_class_input():
    pass

def multi_class_input():
    pass
    

if __name__ == '__main__':
    main()