package de.htwg.modprog.cubic.model.impl

import de.htwg.modprog.cubic.model.{Field => FieldTrait}

class Field(val occupiedBy: Option[Player], val isHighlighted: Boolean) extends FieldTrait