import argparse
import json
from rdflib import Graph, Namespace, URIRef, Literal, BNode
from rdflib.namespace import RDF, XSD


def open_json_file(file_path: str) -> dict:
    """
    Open a JSON file and return its content.
    
    :param file_path: Path to the JSON file.
    :type file_path: str
    
    :return: Parsed JSON content as a dictionary.
    :rtype: dict
    """
    
    with open(file_path, "r", encoding="utf-8") as f:
        return json.load(f)
    
def save_graph_to_turtle(graph: Graph, output_file: str) -> None:
    """
    Serialize the RDF graph to a Turtle file.
    
    :param graph: RDF graph to serialize.
    :type graph: rdflib.Graph
    :param output_file: Path to the output Turtle file.
    :type output_file: str
    """
    
    graph.serialize(destination=output_file, format="turtle")
    print(f"Graph saved to {output_file}")
    
def create_annotation_graph(campaign: dict) -> Graph:
    """
    Create an RDF graph from the campaign annotations.
    
    :param campaign: Dictionary containing campaign data with annotations.
    :type campaign: dict
    
    :return: RDF graph representing the annotations.
    :rtype: rdflib.Graph
    """
    
    # Define namespaces
    DIVA = Namespace("http://example.org/diva#")
    PROV = Namespace("http://www.w3.org/ns/prov#")
    DCTERMS = Namespace( "http://purl.org/dc/terms/")
    OA =  Namespace("http://www.w3.org/ns/oa#")

    # Create RDF graph
    graph = Graph()
    graph.bind("diva", DIVA)
    graph.bind("prov", PROV)
    graph.bind("dct", DCTERMS)

    for annotation in campaign["annotations"]:
        # Create resources
        annotation_id = int(annotation["id"])
        annotation_uri = URIRef(f"http://example.org/annotation/{annotation_id}/")
        item_bnode = BNode()

        # Add annotation metadata
        graph.add((annotation_uri, RDF.type, DIVA.Annotation))
        graph.add((annotation_uri, DIVA.HasCampaignId, Literal(campaign["id"])))
        graph.add((annotation_uri, DIVA.hasAnnotationType, Literal(annotation["annotationType"])))
        graph.add((annotation_uri, DCTERMS.created, Literal(annotation["date"], datatype=XSD.dateTime)))
        graph.add((annotation_uri, DIVA.HasDescription, Literal(annotation["description"])))#description in example.org
        graph.add((annotation_uri, DCTERMS.creator,Literal( annotation["creator"]["email"])))
    
        graph.add((annotation_uri, OA.hasTarget, item_bnode))
        graph.add((item_bnode, RDF.type, DIVA.Annotation))
        graph.add((item_bnode, DIVA.hasAnnotationType, Literal(annotation["item"]["type"])))
        graph.add((item_bnode, DIVA.value, Literal(annotation["item"]["value"])))
        graph.add((item_bnode, DIVA.HasPageUrl, URIRef(annotation["pageUrl"])))

        # Add concepts
        for concept in annotation["concepts"]:
            concept_uri = URIRef(concept["uri"])
            graph.add((annotation_uri, DIVA.hasConcept, concept_uri))
    
    return graph
            
if __name__ == "__main__":
    # Parse command line arguments
    parser = argparse.ArgumentParser(description="Convert JSON annotations to RDF/Turtle format")
    parser.add_argument(
        "-i",
        "--input",
        required=True,
        help="input JSON file path or JSON string"
    )
    parser.add_argument(
        "--is-string",
        default=False,
        action="store_true",
        help="treat input as a JSON string instead of a file path"
    )
    args = parser.parse_args()
    
    if args.is_string:
        # If input is a JSON string, parse it directly
        campaign = json.loads(args.input)
    else:  
        # If input is a file path, load from file
        campaign = open_json_file(args.input)
    
    # Create RDF graph
    graph = create_annotation_graph(campaign)
    
    # Export to Turtle
    save_graph_to_turtle(graph, "campaign.ttl")
