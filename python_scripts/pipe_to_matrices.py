from xml.dom.minidom import parse
import numpy as np
pipe_xml_path = "../rdp_pipe/rdp_PAPER.xml"

document = parse(pipe_xml_path)

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

for i in range(len(places)):
    for j in range(len(transitions)):
        for arc in arcs:
            if arc.attributes["source"].value == places_ids[i] and arc.attributes["target"].value == transitions_ids[j]:
                W_menos[i][j]   = int(arc.getElementsByTagName("inscription")[0].getElementsByTagName("value")[0].firstChild.nodeValue.split(',')[1])
            
            if arc.attributes["target"].value == places_ids[i] and arc.attributes["source"].value == transitions_ids[j]:
                W_mas[i][j]     = int(arc.getElementsByTagName("inscription")[0].getElementsByTagName("value")[0].firstChild.nodeValue.split(',')[1])
            
# vector de tiempos [ [alfa, beta], ... ]
tiempos = [ [float(t.getElementsByTagName("rate")[0].getElementsByTagName("value")[0].firstChild.nodeValue) if t.getElementsByTagName("timed")[0].getElementsByTagName("value")[0].firstChild.nodeValue == "true" else 0, -1] for t in transitions ]




def imprimir_matriz(matriz):
    print("[")
    print("\t#", end=" ")
    for j in range(len(matriz[0])):
        print(f"T{j+1}", end=" ")
    print("")
    for i in range(len(matriz)):
        fila = matriz[i]
        print("\t[", end=" ")
        for elemento in fila:
            print(f"{elemento}", end=", ")
        print(f"], # P{i+1}")
    print("]")

def imprimir_tiempos(matriz):
    print("[")
    print("\t# alfa ,  beta")
    for i in range(len(matriz)):
        fila = matriz[i]
        print("\t[", end=" ")
        for elemento in fila:
            print(f"{elemento:5.1f}", end=", ")
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