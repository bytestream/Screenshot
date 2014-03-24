<?php

header('Content-Type: text/plain; charset=utf-8');

function get_file_size($size) {
  $units = array('Bytes', 'KiB', 'MiB', 'GiB', 'TiB', 'PiB', 'EiB');
  return @round($size / pow(1024, ($i = floor(log($size, 1024)))), 2).' '.$units[$i];
}

try {

    // Undefined | Multiple Files | $_FILES Corruption Attack
    // If this request falls under any of them, treat it invalid.
    if (!isset($_FILES['image']['error'])
            || is_array($_FILES['image']['error']))
    {
        throw new RuntimeException('Invalid parameters.');
    }

    // Check $_FILES['image']['error'] value.
    switch ($_FILES['image']['error']) {
        case UPLOAD_ERR_OK:
            break;
        case UPLOAD_ERR_NO_FILE:
            throw new RuntimeException('No file sent.');
        case UPLOAD_ERR_INI_SIZE:
        case UPLOAD_ERR_FORM_SIZE:
            throw new RuntimeException('Exceeded filesize limit.');
        default:
            throw new RuntimeException('Unknown errors.');
    }

    // You should also check filesize here.
    if ($_FILES['image']['size'] > 1000000) {
        throw new RuntimeException('Exceeded filesize limit.');
    }

    // Check mime type
    $finfo = new finfo(FILEINFO_MIME_TYPE);
    if (false === $ext = array_search(
        $finfo->file($_FILES['image']['tmp_name']),
        array(
            'jpg' => 'image/jpeg',
            'png' => 'image/png',
            'gif' => 'image/gif',
        ),
        true
    )) {
        throw new RuntimeException('Invalid file format.');
    }

    // Create file location
    $fileLocation = sprintf(
        './uploads/%s.%s',
        sha1_file($_FILES['image']['tmp_name']),
        $ext
    );

    // Move uploaded file
    if (!move_uploaded_file($_FILES['image']['tmp_name'], $fileLocation)) {
        throw new RuntimeException('Failed to move uploaded file.');
    }

    // Successful upload
    echo json_encode(array(
        "URL"   => "http://{$_SERVER['SERVER_NAME']}/{$fileLocation}",
        "size"  => get_file_size($_FILES['image']['size'])
    ));

} catch (RuntimeException $e) {
    echo $e->getMessage();
}