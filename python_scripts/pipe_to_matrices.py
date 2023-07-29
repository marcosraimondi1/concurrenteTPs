from xml.dom.minidom import parse
import numpy as np
pipe_xml_path = "../rdp_pipe/rdp_TP2.xml"

document = parse(pipe_xml_path) # analiza la gramatrica

places          = document.getElementsByTagName("place")
transitions     = document.getElementsByTagName("transition")
arcs            = document.getElementsByTagName("arc")

places.sort(key=lambda place: int(place.attributes["id"].value[1:]))
transitions.sort(key=lambda transition: int(transition.attributes["id"].value[1:]))

marcado_inicial = [int(place.getElementsByTagName("initialMarking")[0].getElementsByTagName("value")[0].firstChild.nodeValue.split(',')[1]) for place in places]

places_ids      = [place.attributes["id"].value for place in places]
transitions_ids = [transition.attributes["id"].value for transition in transitions]

# matriz W- : plazas -> transiciones
W_menos = np.zeros((len(places), len(transitions)), dtype=int)  # plazas filas y transiciones columnas

# matriz W+ : transiciones -> plazas
W_mas = np.zeros((len(places), len(transitions)), dtype=int)    # plazas filas y transiciones columnas

# llena las matrices
for i in range(len(places)): # tantas filas como transiciones
    for j in range(len(transitions)): # tantas columnas como plazas
        for arc in arcs:
            if arc.attributes["source"].value == places_ids[i] and arc.attributes["target"].value == transitions_ids[j]:
                W_menos[i][j]   = int(arc.getElementsByTagName("inscription")[0].getElementsByTagName("value")[0].firstChild.nodeValue.split(',')[1])
            
            if arc.attributes["target"].value == places_ids[i] and arc.attributes["source"].value == transitions_ids[j]:
                W_mas[i][j]     = int(arc.getElementsByTagName("inscription")[0].getElementsByTagName("value")[0].firstChild.nodeValue.split(',')[1])
            
# vector de tiempos [ [alfa, beta], ... ]
tiempos = [ [str(int(float(t.getElementsByTagName("rate")[0].getElementsByTagName("value")[0].firstChild.nodeValue)))+"L" if t.getElementsByTagName("timed")[0].getElementsByTagName("value")[0].firstChild.nodeValue == "true" else "0L", "MAX_TIME"] for t in transitions ]

# encontrar conflictos (estructurales)
# las filas son plazas y las columnas son transiciones
conflictos = []
for i in range(len(W_menos)):
    # empiezo tomando una plaza i y veo cuantas transiciones tiene asociadas a la salida
    conflicto = []
    for j in range(len(W_menos[i])):
        if W_menos[i][j] >= 1:
            conflicto.append(j) # agrego todos los indices de las transiciones que tienen asociada la plaza i como entrada
    if len(conflicto) > 1: # si luego de recorrer una fila completa este vector aux es mayor a 1 tengo un conflicto estructural
        if (conflicto not in conflictos):
            conflictos.append(conflicto) # agrego ese conflicto


def imprimir_matriz(matriz):
    print("[")
    print("\t#", end=" ")
    for j in range(len(matriz[0])):
        print(f"T{j}", end=" ")
    print("")
    for i in range(len(matriz)):
        fila = matriz[i]
        print("\t[", end=" ")
        for elemento in fila:
            print(f"{elemento}", end=", ")
        print(f"], # P{i}")
    print("]")

def imprimir_matriz_normal(matriz):
    print("[")
    for fila in matriz:
        print("\t[", end=" ")
        for elemento in fila:
            print(f"{elemento}", end=", ")
        print(f"\t],")
    print("]")

def imprimir_tiempos(matriz):
    print("[")
    print("\t# alfa ,  beta")
    for i in range(len(matriz)):
        fila = matriz[i]
        print("\t[", end=" ")
        for elemento in fila:
            print(f"{elemento:5}", end=", ")
        print(f"], # T{i}")
    print("]")


print("\n✅ MARCADO INICIAL : ")
print("#", end=" ")
for i in range(len(marcado_inicial)):
    print(f"P{i}", end=" ")
print("")
print(marcado_inicial)

print("\n✅ MATRIZ W- : P ➡️ T")
print(imprimir_matriz(W_menos))

print("\n✅ MATRIZ W+ : T ➡️ P")
print(imprimir_matriz(W_mas))

print("\n✅ TIEMPOS : ALFA - BETA")
print(imprimir_tiempos(tiempos))

print("\n✅ CONFLICTOS : TX - TY")
print(imprimir_matriz_normal(conflictos))