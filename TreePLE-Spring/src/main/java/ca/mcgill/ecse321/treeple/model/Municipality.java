/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/

package ca.mcgill.ecse321.treeple.model;
import java.util.*;

import ca.mcgill.ecse321.treeple.model.Tree.Landtype;

// line 32 "../../../../../TreePLE.ump"
public class Municipality
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Municipality Attributes
  private String name;

  //Municipality Associations
  private List<Tree> tree;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Municipality(String aName)
  {
    name = aName;
    tree = new ArrayList<Tree>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public String getName()
  {
    return name;
  }

  public Tree getTree(int index)
  {
    Tree aTree = tree.get(index);
    return aTree;
  }

  public List<Tree> getTree()
  {
    List<Tree> newTree = Collections.unmodifiableList(tree);
    return newTree;
  }

  public int numberOfTree()
  {
    int number = tree.size();
    return number;
  }

  public boolean hasTree()
  {
    boolean has = tree.size() > 0;
    return has;
  }

  public int indexOfTree(Tree aTree)
  {
    int index = tree.indexOf(aTree);
    return index;
  }

  public static int minimumNumberOfTree()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Tree addTree(double aX, double aY, double aHeight, double aDiameter, Landtype aLandType, Species aSpecies)
  {
    return new Tree(aX, aY, aHeight, aDiameter, aLandType, aSpecies, this);
  }

  public boolean addTree(Tree aTree)
  {
    boolean wasAdded = false;
    if (tree.contains(aTree)) { return false; }
    Municipality existingMunicipality = aTree.getMunicipality();
    boolean isNewMunicipality = existingMunicipality != null && !this.equals(existingMunicipality);
    if (isNewMunicipality)
    {
      aTree.setMunicipality(this);
    }
    else
    {
      tree.add(aTree);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeTree(Tree aTree)
  {
    boolean wasRemoved = false;
    //Unable to remove aTree, as it must always have a municipality
    if (!this.equals(aTree.getMunicipality()))
    {
      tree.remove(aTree);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  public boolean addTreeAt(Tree aTree, int index)
  {  
    boolean wasAdded = false;
    if(addTree(aTree))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTree()) { index = numberOfTree() - 1; }
      tree.remove(aTree);
      tree.add(index, aTree);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveTreeAt(Tree aTree, int index)
  {
    boolean wasAdded = false;
    if(tree.contains(aTree))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTree()) { index = numberOfTree() - 1; }
      tree.remove(aTree);
      tree.add(index, aTree);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addTreeAt(aTree, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    for(int i=tree.size(); i > 0; i--)
    {
      Tree aTree = tree.get(i - 1);
      aTree.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "]";
  }
}