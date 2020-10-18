 <?php
$class = 'Pet';
require $class . '.php';

$meta = [
  'properties' => [
    'birth' => [
      'type' => 'DATE',
      'control' => [
        'element' => 'input',
        'type' => 'date'
      ]
    ],
    'death' => [
      'type' => 'DATE',
      'control' => [
        'element' => 'input',
        'type' => 'date'
      ]
    ],
    'sex' => [
      'type' => 'CHAR(1)',
      'control' => [
        'element' => 'select',
        'options' => ['f', 'm']
      ]
    ],
    'species' => [
      'control' => [
        'element' => 'select',
        'options' => ['bird', 'cat', 'dog', 'hamster', 'snake']
      ]
    ]
  ]
];

$config = parse_ini_file('config.ini');

$dsn = "mysql:host=${config['host']};dbname=${config['db']};charset=utf8mb4";
$options = [
  PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
  PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
  PDO::ATTR_EMULATE_PREPARES => false
];
$pdo = new PDO($dsn, $config['user'], $config['pass'], $options);

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
    return $prop->getName() . ' ' . ($meta['properties'][$prop->getName()]['type'] ?? 'VARCHAR(20)');
  }, $props));
  $pdo->exec("CREATE TABLE $table ($cols)");

  $stmt = $pdo->query("SELECT * FROM $table");
}

$items = $stmt->fetchAll(PDO::FETCH_CLASS, $class);

include 'html.php';
?>
