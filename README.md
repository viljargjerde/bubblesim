# Bubble simulation

This is a program for simulating the the creation and movement of bubbles in an electrolyser. A short introduction can be seen [here](https://drive.google.com/file/d/1r3Sq5wjamU8zsynjGcs_H7Bnh5L_u4LF/view?usp=sharing). A longer simulation can be seen [here](https://drive.google.com/file/d/1OKz8aSZDiQyGGtM__VW4JjoypDiEIG5Y/view?usp=sharing)

## Description

This simulation is based on the forces acting on bubbles described in [this paper](https://www.sciencedirect.com/science/article/abs/pii/S0017931018318404). This project has been made in cooperation with HVL, who have provided the necessary physics simulation. All programming is done by me. The simulation is made to increase understanding of how the working conditions inside an electrolyser cell affects the bubbles formed, and how one can efficiently remove bubbles from the catalyst layer. In particular it models how the use of rotation to induce an artificial gravity field can be beneficial. Although electrolysis is the main target for this program, it will also be able to simulate the formation of droplets on the catalyst layer in a fuel-cell, but this is not implemented at this moment. 

When the program is started there are two squares representing electrodes seen from above. When an electrode is started blue bubbles are created on the surface and when they reach a big enough size they start moving according to the forces described below. 
 
### Modeled forces

Currently no forces acting in the z-direction are modeled. This is because the most relevant forces all act along the electrode. The forces modeled are

- Buoyancy due to gravity 
- Gravity
- Buoyancy due to rotation
- Centripetal force due to rotation
- Drag force
- Resistance to motion due to surface tension force. 

## Installation 
The main program is ready to run on windows PCs, and all that is needed to do is to download the .jar file and run it. The program also includes a script written in python (from previous projects) that is used to combine recorded frames into a video. In order to use this script it is necessary to install a few modules: 
```
python -m pip install opencv-python
```

```
pip install tk
```
and
```
pip install pyautogui
```
## Usage

To run the program, download the .jar file, and place it in a desired folder. Note that a new folder called "frames" will be created in the same directory, where each created frame is saved. To use the python script to make a video, download the .py file, and run 
```
python combine_frames_to_video.py
```
in the same folder as the python file is, and then select the frames folder.

When the program is launched you can change parameters, and when you are happy you can start one or both of the electrodes. It is possible at any time to change any parameter or pause and unpause one or both of the electrodes. This is done by selecting the parameter you want to change, type the desired change (use , for decimal), and pressing enter. The units are as follows:
- Pressure: Bar
- Temperature: Celsius
- Distance from center: meter
- RPM: RPM

In addition to changing parameters it is also possible to zoom up to 10x. It should be noted that zooming is not instant, but rather a process taking some frames. This is done to make the resulting video more smooth and make it easier to see what is happening. The downside is that it makes zooming feel slow when using the program. In general most things feel slow, and each change of zoom or of a parameter should be left to run for a good while. While the simulation is running on its own, it is often useful to minimize the window. This does not affect the frames saved, but improves speed and frees up your screen.

The electrode on the left produces oxygen bubbles in water and the electrode on the right produces hydrogen bubbles in water. Whenever one or both of the electrodes are simulating, a screenshot will be taken each time-step. One time-step is 1/6000 of a second. This is due to a slowdown factor of 200, meaning that 200 seconds of video corresponds to 1 second in realtime. The video is then played back at 30fps. Resizing of window should be done before either of the electrodes are started to avoid saving frames with different sizes.


## Future improvements
This simulation was made as a semester assignment, but was also made with the intention of improving the capabilities and usability after the semester assignment is finished. Here are some of the potential additions

- Ability to change the fluid produced and the surrounding fluid from UI.
- Ability to change number and mass of produced spheroids from UI.
- Ability to change slowdown factor.
- Ability to set fluid velocity inside electrode.
- Support for simulation of drops
- More multithreading and/or GPU support to improve performance and accuracy.
- More responsive GUI by using BufferedImage to skip redrawing simulation every frame, and only redraw after each step is completed
- A fixed time-step updater.
- Model forces in z-direction
- More realistic collision behavior.  


## Author
Viljar Helgestad Gjerde

## References
- Du, J., Zhao, C., & Bo, H. (2018). Investigation of bubble departure diameter in horizontal and vertical subcooled flow boiling. Int. J. Heat Mass Transfer, 127, 796–805. doi: 10.1016/j.ijheatmasstransfer.2018.07.019
- Jaroslaw Drelich; Jan D. Miller (1994). The Effect of Solid Surface Heterogeneity and Roughness on the Contact Angle/Drop (Bubble) Size Relationship. , 164(1), 252–259. doi:10.1006/jcis.1994.1164 
- Xue, J., Shi, P., Zhu, L., Ding, J., Chen, Q., & Wang, Q. (2014). A modified captive bubble method for determining advancing and receding contact angles. Appl. Surf. Sci., 296, 133–139. doi: 10.1016/j.apsusc.2014.01.060




