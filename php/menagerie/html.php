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
    <?php $form = "update$i"; ?>
    <tr>
      <?php foreach ($props as $prop) { ?>
        <td>
          <?php include 'control.php'; ?>
        </td>
      <?php } ?>
      <td>
        <form id="<?php echo $form; ?>" method="post" action="<?php echo htmlspecialchars($_SERVER['PHP_SELF']); ?>">
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

  <?php $item = null; ?>
  <?php $form = "create"; ?>
  <tr>
    <?php foreach ($props as $prop) { ?>
      <td>
        <?php include 'control.php'; ?>
      </td>
    <?php } ?>
    <td>
      <form id="<?php echo $form; ?>" method="post" action="<?php echo htmlspecialchars($_SERVER['PHP_SELF']); ?>">
        <input type="submit" name="insert" value="insert">
      </form>
    </td>
  </tr>
</table>

</body>
</html>
