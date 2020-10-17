 <?php

$config = parse_ini_file('config.ini');

$dsn = "mysql:host=${config['host']};dbname=${config['db']};charset=utf8mb4";
$options = [
  PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
  PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
  PDO::ATTR_EMULATE_PREPARES => false
];
$pdo = new PDO($dsn, $config['user'], $config['pass'], $options);

class Pet {
  public ?string $name;
  public ?string $owner;
  public ?string $species;
  public ?string $sex;
  public ?string $birth;
  public ?string $death;
}

$class = 'Pet';
$table = strtolower($class);
$reflect = new ReflectionClass($class);
$props = $reflect->getProperties();

if (isset($_POST['insert'])) {
  $names = implode(', ', array_map(function($prop) {
    return $prop->getName();
  }, $props));
  $values = implode(', ', array_map(function($prop) {
    return '?';
  }, $props));
  $params = array_map(function($prop) {
    $param = $_POST[$prop->getName()];
    return !empty($param) ? $param : null;
  }, $props);
  $stmt = $pdo->prepare("INSERT INTO $table ($names) VALUES ($values)");
  $stmt->execute($params);
}

if (isset($_POST['update'])) {
  $names = array_map(function($prop) {
    return $prop->getName() . '=?';
  }, $props);
  $set = implode(', ', $names);
  $where = $names[0];
  $params = array_map(function($prop) {
    $param = $_POST[$prop->getName()];
    return !empty($param) ? $param : null;
  }, $props);
  $params[] = $_POST['_' . $props[0]->getName()];
  $stmt = $pdo->prepare("UPDATE $table SET $set WHERE $where");
  $stmt->execute($params);
}

if (isset($_POST['delete'])) {
  $where = $props[0]->getName() . '=?';
  $params = [$_POST['_' . $props[0]->getName()]];
  $stmt = $pdo->prepare("DELETE FROM $table WHERE $where");
  $stmt->execute($params);
}

try {
  $stmt = $pdo->query("SELECT * FROM $table");
} catch (\PDOException $e) {
  $cols = implode(', ', array_map(function($prop) {
    return $prop->getName() . ' ' . ($prop->getType()->getName() === 'int' ? 'TINYINT(1)' : 'VARCHAR(1000)');
  }, $props));
  $pdo->exec("CREATE TABLE $table ($cols)");

  $stmt = $pdo->query("SELECT * FROM $table");
}

$items = $stmt->fetchAll(PDO::FETCH_CLASS, $class);
?>

<!DOCTYPE html>
<html>
<head>
<style>
table {
  font-family: arial, sans-serif;
  border-collapse: collapse;
}

td, th {
  border: 1px solid #dddddd;
  text-align: left;
  padding: 8px;
}

tr:nth-child(even) {
  background-color: #dddddd;
}
</style>
</head>
<body>

<h2>Menagerie</h2>

<table>
  <tr>
    <?php foreach ($props as $i=>$prop) { ?>
      <th><?php echo $prop->getName(); ?></th>
    <?php } ?>
    <th></th>
  </tr>

  <?php foreach ($items as $i=>$item) { ?>
    <tr>
      <?php foreach ($props as $prop) { ?>
        <td>
          <input
            form="<?php echo "update$i"; ?>"
            type="text"
            name="<?php echo $prop->getName(); ?>"
            value="<?php echo $prop->getValue($item); ?>"
            >
        </td>
      <?php } ?>
      <td>
        <form id="<?php echo "update$i"; ?>" method="post" action="<?php echo htmlspecialchars($_SERVER['PHP_SELF']); ?>">
          <input
            type="hidden"
            name="<?php echo '_' . $props[0]->getName(); ?>"
            value="<?php echo $props[0]->getValue($item); ?>"
            >
          <input type="submit" name="update" value="update">
          <input type="submit" name="delete" value="delete">
        </form>
      </td>
    </tr>
  <?php } ?>

  <tr>
    <?php foreach ($props as $prop) { ?>
      <td>
        <input form="create" type="text" name="<?php echo $prop->getName(); ?>">
      </td>
    <?php } ?>
    <td>
      <form id="create" method="post" action="<?php echo htmlspecialchars($_SERVER['PHP_SELF']); ?>">
        <input type="submit" name="insert" value="insert">
      </form>
    </td>
  </tr>
</table>

</body>
</html>
