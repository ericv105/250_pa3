/**
 * GroupByStore.scala
 *
 * Copyright 2019 Andrew Hughes (ahughes6@buffalo.edu)
 *
 * This work is licensed under the Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International License.
 * To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc-sa/4.0/.
 *
 * Submission author
 * UBIT: ericvele
 * Person#: 50263658
 *
 * Collaborators (include UBIT name of each, comma separated):
 * UBIT:
 */
package cse250.pa3

import cse250.objects.{StreetGraph, TaxEntry}

import scala.collection.mutable
import scala.xml.{PrettyPrinter, XML}

object MapUtilities {
  def loadIntersectionIDs(filename: String): mutable.Set[String] = {
    val xml = XML.loadFile(filename)
    val node: scala.xml.Elem = xml
    val nodeIDs: List[String] = (node \ "node").map(node => node \@ "id").toList
    mutable.Set[String]() ++ nodeIDs
  }

  def loadMapInfo(filename: String): mutable.Map[String, mutable.Set[String]] = {
    val ret = mutable.Map[String, mutable.Set[String]]()
    val xml = XML.loadFile(filename)
    for{
      way <- xml \\ "way"
      tag <- way \ "tag"
      ndid <- way \ "nd"
    } yield {
      val value: String = (tag \ "@v").toString.toUpperCase()
      if((tag \ "@k").toString() == "tiger:name_base") {
        val key: String = (ndid \ "@ref").toString
        if(!ret.contains(key)){
          ret(key) = mutable.Set(value)
        } else {
          ret(key) += value
        }
      }
    }
    ret
  }

  def buildIntersectionGraph(intersectionIDs: mutable.Set[String],
                             nodeToStreetMapping: mutable.Map[String, mutable.Set[String]]): StreetGraph = {
    val streetGraph = new StreetGraph

    for(id <- intersectionIDs){
      if(nodeToStreetMapping.contains(id) && nodeToStreetMapping(id).size > 1) {
        //nodeToStreetMapping(id)
        //streetGraph.insertEdge()
        val iter = nodeToStreetMapping(id).toArray.combinations(2)
        while(iter.hasNext){
          val intersection = iter.next()
          val street1 = intersection(0)
          val street2 = intersection(1)
          streetGraph.insertEdge(street1, street2)
          streetGraph.insertEdge(street2, street1)
        }
      }
    }
    streetGraph
  }

  def computeFewestTurns(streetGraph: StreetGraph, start: TaxEntry, end: TaxEntry): Int = {
    -1
  }

  def computeFewestTurnsList(streetGraph: StreetGraph, start: TaxEntry, end: TaxEntry): Seq[String] = {
    List()
  }
}
