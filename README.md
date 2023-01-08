# Evolution simulator

## About
Evolution simulator is an application that was made as a project for second's year subject: Objective Programming.
All the requirements for this app are placed in another 
[Github](https://github.com/apohllo/obiektowe-lab/tree/master/proj1) repository (PL).

## Main concept
The general feature of the application is the thing that the user can simulate the process of evolution. Only
thing you have to do before is selecting configuration file with which app will start.

Application allows to run in the same time few simulations in different windows.

## Configuration
User has possibility to select either one of three previously created config files:
    
    - huge world - big starvation and amount of new && dead animals
    - medium world - a lot of plants, medium world
    - small world - small map with few animals

or to prepare and load own configuration file. File should look like this below

```text
mapWidth=50
mapHeight=50
startPlantsNumber=100
plantEnergy=5
newPlantsEveryDay=20
animalsStartNumber=25
startEnergy=50
energyNeedToReproduce=50
energyWastedDuringReproduction=40
minimalMutationNumber=0
maximalMutationNumber=4
genomeLength=8
mapVariant=1
plantGrowVariant=1
mutationVariant=1
animalBehaveVariant=1
```
Each of parameter should be specified and what is more:

    - every parameter expect minimalMutationNumber and variants should be
      greater than 0
    - variants should have value 0 or 1
    - energy needed to reproduction should be greater than energy wasted 
      during it

## Map data
During simulation, it is shown a panel that presents current parameters of map (day, animals number,
plants number, etc.)

There is also possibility to look at information about selected animal. To do so simply stop simulation
and select animal you want to observe.

## CSV handling
Application let us storing simulation data in CSV files. However, before you will get your files, 
you need to select this option in main menu (before starting).

Files are stored in `/main/resources/csv` and are always deleted before application start.