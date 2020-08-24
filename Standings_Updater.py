# -*- coding: utf-8 -*-
"""
Spyder Editor

This is a temporary script file.
"""

import os, sys

systPts = "Top20"

def F1Points(pos):
    pts=[25, 18, 15, 12, 10, 8, 6, 4, 2, 1]
    if (pos<=10):
        return pts[pos-1]
    else:
        return 0

def Top20Points(pos):
    return 21-pos

def points(syst, pos):
    if (syst=="F1"):
        return F1Points(pos)
    if (syst=="Top20"):
        return Top20Points(pos)

def teamIsIntheTab(name, tab):
    if len(tab)==0:
        return -1
    for i in range(0, len(tab)):
        if tab[i][1]==name:
            return i
    return -1
    
def convStringPointsToInt(p):
    return int(p[:len(p)-2])

def updateStandingsPostRace(league, season, raceName):
    path = ""
    if season == "":
        path = "" + league
    else:
        path = "" + league + "\\" + season
    
    #opening the race result's file
    f1 = open(path+"\\"+raceName+".txt", "r")
    results=f1.read()
    f1.close()

    #if the race has already been saved, we don't register it
    if results[len(results)-5:]=="SAVED":
        print(raceName + " has been already saved")
        return 0
    
#    print(results)
    
    #getting the results into a list
    res = results.split("\n")
    tempList=[]
    for i in range(0,len(res)):
        res[i]=res[i].split("-")
        if len(res[i]) == 1:
            tempList.append(i)
    while len(tempList) > 0:
        del res[tempList.pop(len(tempList)-1)]
         
#    print(res)
    
    #teams' points for the race
    standT=[]
    for i in range(0, len(res)):
        tName=res[i][2]
        teamPts = points(systPts,int(res[i][0]))
        if teamIsIntheTab(tName, standT)<0:
            standT.append([str(len(standT)+1), tName, str(teamPts)])
        else:
            standT[teamIsIntheTab(tName, standT)][2]=str( int(standT[teamIsIntheTab(tName, standT)][2]) + teamPts)
    if len(standT) > 10:
        raise Exception("Too much teams")
#    print(standT)
    
    #opening drivers standings file
    f2 = open(path+"\\Standings\\Drivers.txt", "r")
    driverStandings=f2.read()
    f2.close()
    #if it's the first race
    if(driverStandings==""):
        f3 = open(path+"\\Standings\\Drivers.txt", "a")
        f3.write(raceName+"\n")
        for i in range (0, len(res)):
            s = "-".join(res[i])+"-"+str(points(systPts,int(res[i][0])))+"p."
            if i<len(res)-1:
                s+="\n"
            f3.write(s)
        f3.close()
    #if not the first race
    else:
        #getting the actual drivers' standings
        tabDriversStandings = driverStandings.split("\n")
        tabDriversStandings[0]=tabDriversStandings[0].split(",")
        for i in range(1, len(tabDriversStandings)):
            tabDriversStandings[i] = tabDriversStandings[i].split("-")
#        print(tabDriversStandings)
        
        #updating the standings
        for i in range(0, len(res)):
            isNewDriver = True
            driverName = res[i][1]
            teamName = res[i][2]
            driverPoints = points(systPts, int(res[i][0]))
            for i in range(1, len(tabDriversStandings)):
                if tabDriversStandings[i][1] == driverName and tabDriversStandings[i][2] == teamName:
                    tabDriversStandings[i][3] = str( convStringPointsToInt(tabDriversStandings[i][3]) + driverPoints ) + "p."
                    isNewDriver = False
            if isNewDriver : 
                pos = len(tabDriversStandings)
                tabDriversStandings.append([str(pos), driverName, teamName, str(driverPoints)+"p."])
        tabDriversStandings[0].append(raceName)
#        print(tabDriversStandings)
        f4 = open(path+"\\Standings\\Drivers.txt", "w")
        f4.write(",".join(tabDriversStandings[0]) + "\n")
        for i in range (1, len(tabDriversStandings)):
            f4.write("-".join(tabDriversStandings[i]))
            if i<len(tabDriversStandings)-1:
                f4.write("\n")
        f4.close()
        
    #opening teams standings file
    f5 = open(path+"\\Standings\\Teams.txt", "r")
    teamStandings=f5.read()
    f5.close()
    #if it's the first race
    if(teamStandings==""):
        f6 = open(path+"\\Standings\\Teams.txt", "a")
        f6.write(raceName+"\n")
        for i in range (0, len(standT)):
            s = "-".join(standT[i]) +"p."
            if i<len(standT)-1:
                s+="\n"
            f6.write(s)
        f6.close()
    #if it's not the first race
    else:
        #getting the actual teams' standings
        tabTeamStandings = teamStandings.split("\n")
        tabTeamStandings[0]=tabTeamStandings[0].split(",")
        for i in range(1, len(tabTeamStandings)):
            tabTeamStandings[i] = tabTeamStandings[i].split("-")
#        print(tabTeamStandings)
            
        #updating the standings
        for i in range(0, len(standT)):
            isNewTeam = True
            teamName = standT[i][1]
            teamPoints = int(standT[i][2])
            for i in range(1, len(tabTeamStandings)):
                if tabTeamStandings[i][1] == teamName:
                    tabTeamStandings[i][2] = str ( convStringPointsToInt(tabTeamStandings[i][2]) + teamPoints) + "p."
                    isNewTeam = False
            if isNewTeam :
                pos = len(tabTeamStandings)
                tabTeamStandings.append([str(pos), teamName, str(teamPoints)+"p."])
        tabTeamStandings[0].append(raceName)
        
        f7 = open(path+"\\Standings\\Teams.txt", "w")
        f7.write(",".join(tabTeamStandings[0]) + "\n")
        for i in range (1, len(tabTeamStandings)):
            f7.write("-".join(tabTeamStandings[i]))
            if i<len(tabTeamStandings)-1:
                f7.write("\n")
        f7.close()
    
    f8 = open(path+"\\"+raceName+".txt", "a")
    f8.write("\nSAVED")
    f8.close()
    print(raceName + " added!")
        
def updateFullChampionship (league, season):
    path = "" + league + "\\" + season
    dirs = os.listdir( path )
    for name in dirs :
        if name[len(name)-4:]==".txt":
#            print(name)
            updateStandingsPostRace(league, season, name[:len(name)-4])
            
updateFullChampionship("F2 League", "Season 1")
