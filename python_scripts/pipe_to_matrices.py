from xml.dom.minidom import parse
import numpy as np
pipe_xml_path = "C:\\Users\\marco\\Downloads\\FinalPetri_v2.xml"

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
            



def imprimir_matriz(matriz):
    print("[")
    for fila in matriz:
        print("\t[", end=" ")
        for elemento in fila:
            print(elemento, end=", ")
        print("],")
    print("]")


print("\n✅ MARCADO INICIAL : ")
print(marcado_inicial)

print("\n✅ MATRIZ W- : ")
print(imprimir_matriz(W_menos))

print("\n✅ MATRIZ W+ : ")
print(imprimir_matriz(W_mas))
