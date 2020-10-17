 <?php

$config = parse_ini_file('config.ini');

$dsn = "mysql:host=${config['host']};dbname=${config['db']};charset=utf8mb4";
$options = [
  PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
  PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
  PDO::ATTR_EMULATE_PREPARES => false
];
$pdo = new PDO($dsn, $config['user'], $config['pass'], $options);

switch ($_POST['action'] ?? '') {
  case 'create':
    $stmt = $pdo->prepare('INSERT INTO task (name, completed) VALUES (?, ?)');
    $stmt->execute([$_POST['name'], (int) false]);
    break;
  case 'update':
    $stmt = $pdo->prepare('UPDATE task SET completed=? WHERE name=?');
    $stmt->execute([(int) $_POST['completed'], $_POST['name']]);
    break;
}

try {
  $stmt = $pdo->query('SELECT name, completed FROM task');
} catch (\PDOException $e) {
  $pdo->exec('CREATE TABLE task (name varchar(1000), completed tinyint(1))');
  $stmt = $pdo->query('SELECT name, completed FROM task');
}

class Task {
  public $name;
  public $completed;
}

$tasks = $stmt->fetchAll(PDO::FETCH_CLASS, 'Task');
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

<h2>Time management</h2>

<table>
  <tr>
    <th>Task</th>
    <th>Completed</th>
  </tr>

<?php foreach ($tasks as $task) { ?>
  <tr>
    <td><?php echo $task->name; ?></td>
    <td>
      <form method="post" action="<?php echo htmlspecialchars($_SERVER['PHP_SELF']);?>">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="name" value="<?php echo htmlspecialchars($task->name);?>">
        <input type="checkbox" name="completed" value="<?php echo $task->completed ? '' : '1';?>" <?php echo $task->completed ? 'checked' : ''; ?> onclick="this.form.submit()">
      </form>
    </td>
  </tr>
<?php } ?>

  <tr>
    <td>
      <form method="post" action="<?php echo htmlspecialchars($_SERVER['PHP_SELF']);?>">
        <input type="hidden" name="action" value="create">
        <input type="text" name="name">
      </form>
    </td>
    <td>
    </td>
  </tr>
</table>

</body>
</html>
