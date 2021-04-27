package io.github.thesummergrinch.mcmanhunt.io;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Used to recursively delete world-files and folders, in the event that an
 * MCManHunt-game ends.
 */
public final class WorldDirectoryFileVisitor implements FileVisitor<Path> {

    /**
     * {@inheritDoc}
     */
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {

        return FileVisitResult.CONTINUE;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

        if (attrs.isRegularFile()) {

            Files.delete(file);

        }

        return FileVisitResult.CONTINUE;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {

        return FileVisitResult.TERMINATE;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {

        Files.delete(dir);

        return FileVisitResult.CONTINUE;

    }
}