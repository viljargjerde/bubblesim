from os.path import isfile
import os
import cv2
from tkinter import Tk, filedialog
import pyautogui as pg

def create_video(path):
    delete_frames = input("Do you want to delete frames after creating video? (y|n)").lower()
    img = cv2.imread(path+"/40"+".png")
    height, width, layers = img.shape
    size = (width,height)
    out = cv2.VideoWriter(path+'/simulation.mp4',cv2.VideoWriter_fourcc(*'DIVX'), 30, size)
    i = 0
    while isfile(path+"/"+str(i)+".png"):
        img = cv2.imread(path+"/"+str(i)+".png")
        h, w, layers = img.shape
        if not size == (w,h):
            cv2.resize(img,size)
        out.write(img)
        if delete_frames == "y":
            os.remove(path+"/"+str(i)+".png")
        i+=1
 
    out.release()


def get_folder():
    root = Tk() 
    root.withdraw() 
    root.attributes('-topmost', True) 
    folder = filedialog.askdirectory()+"/"
    pg.hotkey("alt","tab")
    return folder


path = get_folder()

create_video(path)