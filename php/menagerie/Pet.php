 <?php

class Pet {

  public $name;

  public $owner;

  /**
  * [
  *   'control' => [
  *     'element' => 'select',
  *     'options' => ['bird', 'cat', 'dog', 'hamster', 'snake']
  *   ]
  * ]
  */
  public $species;

  /**
  * [
  *   'control' => [
  *     'element' => 'select',
  *     'options' => ['f', 'm']
  *   ]
  * ]
  */
  public $sex;

  /**
  * [
  *   'type' => 'DATE',
  *   'control' => [
  *     'element' => 'input',
  *     'type' => 'date'
  *   ]
  * ]
  */
  public $birth;

  /**
  * [
  *   'type' => 'DATE',
  *   'control' => [
  *     'element' => 'input',
  *     'type' => 'date'
  *   ]
  * ]
  */
  public $death;

}
