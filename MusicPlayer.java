import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MusicPlayer {
    private JFrame frame;
    private JLabel songLabel;
    private JLabel albumCoverLabel;
    private JProgressBar progressBar;
    private JSlider volumeSlider;
    private JButton playButton;
    private JButton pauseButton;
    private JButton stopButton;
    private JButton nextButton;
    private JButton previousButton;
    private JButton shuffleButton;
    private JButton showPlaylistButton;
    private JTextField trackNumberField;
    private JButton changeTrackButton;
    private List<File> songList;
    private List<ImageIcon> albumCoverList;
    private int currentSongIndex;
    private Clip clip;
    private FloatControl volumeControl;
    private Timer timer;
    private VisualizationPanel visualizationPanel;
    private AudioInputStream audioInputStream;
    private Random random;

    public MusicPlayer() {
        random = new Random();
        initializeUI();
        songList = new ArrayList<>();
        albumCoverList = new ArrayList<>();
        loadPlaylist();
        currentSongIndex = songList.isEmpty() ? -1 : 0;
        frame.setVisible(true);
    }
    

    private void initializeUI() {
        frame = new JFrame("Music Player");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        songLabel = new JLabel("No song selected", SwingConstants.CENTER);
        songLabel.setFont(new Font("Arial", Font.BOLD, 16));
        frame.add(songLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        albumCoverLabel = new JLabel();
        albumCoverLabel.setHorizontalAlignment(SwingConstants.CENTER);
        albumCoverLabel.setPreferredSize(new Dimension(300, 300));
        centerPanel.add(albumCoverLabel, BorderLayout.NORTH);

        visualizationPanel = new VisualizationPanel();
        centerPanel.add(visualizationPanel, BorderLayout.CENTER);

        frame.add(centerPanel, BorderLayout.CENTER);

        JPanel controlsPanel = new JPanel(new GridLayout(2, 1));
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        JPanel additionalPanel = new JPanel(new FlowLayout());

        playButton = createButton("Play", "Play the current track", new PlayButtonListener());
        buttonsPanel.add(playButton);

        pauseButton = createButton("Pause", "Pause the current track", new PauseButtonListener());
        buttonsPanel.add(pauseButton);

        stopButton = createButton("Stop", "Stop the current track", new StopButtonListener());
        buttonsPanel.add(stopButton);

        nextButton = createButton("Next", "Play the next track", new NextButtonListener());
        buttonsPanel.add(nextButton);

        previousButton = createButton("Previous", "Play the previous track", new PreviousButtonListener());
        buttonsPanel.add(previousButton);

        shuffleButton = createButton("Shuffle", "Play a random track", new ShuffleButtonListener());
        buttonsPanel.add(shuffleButton);

        showPlaylistButton = createButton("Show Playlist", "Show the playlist", new ShowPlaylistButtonListener());
        buttonsPanel.add(showPlaylistButton);

        trackNumberField = new JTextField(5);
        additionalPanel.add(trackNumberField);

        changeTrackButton = createButton("Change Track", "Change to the specified track number", new ChangeTrackButtonListener());
        additionalPanel.add(changeTrackButton);

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        additionalPanel.add(progressBar);

        volumeSlider = new JSlider(0, 100, 50);
        volumeSlider.setToolTipText("Adjust the volume");
        volumeSlider.addChangeListener(new VolumeSliderListener());
        additionalPanel.add(volumeSlider);

        controlsPanel.add(buttonsPanel);
        controlsPanel.add(additionalPanel);

        frame.add(controlsPanel, BorderLayout.SOUTH);
        frame.pack();
    }

    private JButton createButton(String text, String toolTip, ActionListener listener) {
        JButton button = new JButton(text);
        button.setToolTipText(toolTip);
        button.addActionListener(listener);
        return button;
    }

    private void loadPlaylist() {
        String[] filePaths = {
            "C:\\Users\\vaibh\\Downloads\\compressed\\Ab-Soul, Joey Bada - MOONSHOOTER.wav",
            "C:\\Users\\vaibh\\Downloads\\compressed\\¥, Kanye West, Ty Dolla ign, Rich The Kid, Playboi Carti - CARNIVAL.wav",
            "D:\\21 Savage - 1.5.wav",
            "D:\\21 Savage - a lot.wav",
            "D:\\50 Cent - Many Men (Wish Death).wav",
            "D:\\04. Otis (Ft. Otis Redding).wav",
            "D:\\A AP Rocky - Everyday (feat. Rod Stewart, Miguel & Mark Ronson) (online-audio-converter.com).wav",
            "D:\\Backstreet Boys - Everybody (Backstreets Back) - Radio Edit.wav",
            "D:\\Bruno Mars - 24K Magic.wav",
            "D:\\Drake - 8am in Charlotte.wav",
            "D:\\Drake, 21 Savage - Rich Flex.wav",
            "D:\\Gotye, Kimbra - Somebody That I Used To Know.wav",
            "D:\\Drake - Laugh Now Cry Later (feat. Lil Durk).wav",
            "D:\\Travis Scott - 90210 (feat. Kacy Hill).wav",
            "D:\\Isaiah Rashad - Wats Wrong (feat. Zacari & Kendrick Lamar).wav",
            "D:\\J. Cole - Trae The Truth in Ibiza.wav",
            "D:\\J. Cole - Crocodile Tearz.wav",
            "D:\\Kanye West - Black Skinhead.wav",
            "D:\\Kanye West - Hell Of A Life.wav",
            "D:\\Kanye West - Ultralight Beam.wav",
            "D:\\Kanye West - We Dont Care.wav",
            "D:\\Kanye West, GLC, Consequence - Spaceship.wav",
            "D:\\Kanye West, Jamie Foxx - Gold Digger.wav",
            "D:\\Tyler, The Creator - EARFQUAKE.wav",
            "D:\\See Me Again v1 (Reprod Tobes LxGalaxy AI) [GjuxR9qXqeY].wav"
        };
        
        String[] coverPaths = {
            "C:\\Work And Projects\\Java Codes\\images.jpg",
            "C:\\Work And Projects\\Java Codes\\images (1).jpg",
            "D:\\Album Covers\\download (1).jpeg",
            "D:\\Album Covers\\download (1).jpeg",
            "D:\\Album Covers\\download (2).jpeg",
            "D:\\Album Covers\\download.jpeg",
            "D:\\Album Covers\\AtLongLastASAPCover.jpg",
            "D:\\Album Covers\\download (3).jpeg",
            "D:\\Album Covers\\download (4).jpeg",
            "D:\\Album Covers\\images.jpeg",
            "D:\\Album Covers\\download (6).jpeg",
            "D:\\Album Covers\\download (7).jpeg",
            "D:\\Album Covers\\download (5).jpeg",
            "D:\\Album Covers\\Rodeo_-_Album_Cover_by_Travis_Scott,_September_4,_2015.jpg",
            "D:\\Album Covers\\download (8).jpeg",
            "D:\\Album Covers\\download (9).jpeg",
            "D:\\Album Covers\\download (9).jpeg",
            "D:\\Album Covers\\download (10).jpeg",
            "D:\\Album Covers\\download (11).jpeg",
            "D:\\Album Covers\\download (12).jpeg",
            "D:\\Album Covers\\download (13).jpeg",
            "D:\\Album Covers\\download (13).jpeg",
            "D:\\Album Covers\\download (15).jpeg",
            "D:\\Album Covers\\Igor_-_Tyler,_the_Creator.jpg",
            "D:\\Album Covers\\See Me Again.jpeg"
        };
        

        if (filePaths.length != coverPaths.length) {
            JOptionPane.showMessageDialog(frame, "Mismatch between number of songs and album covers.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1); // Exit the program with an error code
        }
        songList = new ArrayList<>();
        albumCoverList = new ArrayList<>();

        for (int i = 0; i < filePaths.length; i++) {
            File songFile = new File(filePaths[i]);
            File coverFile = new File(coverPaths[i]);
            if (songFile.exists()) {
                songList.add(songFile);
                albumCoverList.add(coverFile.exists() ? new ImageIcon(coverFile.getAbsolutePath()) : null);
            } else {
                System.err.println("File not found: " + filePaths[i]);
            }
        }

        songLabel.setText(songList.isEmpty() ? "No valid songs in the playlist." : "Playlist loaded. Ready to play.");
    }

    private class PlayButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentSongIndex != -1) {
                playSong();
            }
        }
    }

    private class PauseButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (clip != null && clip.isRunning()) {
                clip.stop();
            }
        }
    }

    private class StopButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            stopPlayback();
        }
    }

    private class NextButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentSongIndex < songList.size() - 1) {
                currentSongIndex++;
                playSong();
            }
        }
    }

    private class PreviousButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentSongIndex > 0) {
                currentSongIndex--;
                playSong();
            }
        }
    }

    private class ShuffleButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!songList.isEmpty()) {
                currentSongIndex = random.nextInt(songList.size());
                playSong();
            }
        }
    }

    private class ShowPlaylistButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showPlaylist();
        }
    }

    private class ChangeTrackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int trackNumber = Integer.parseInt(trackNumberField.getText()) - 1;
                if (trackNumber >= 0 && trackNumber < songList.size()) {
                    currentSongIndex = trackNumber;
                    playSong();
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid track number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a valid track number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class VolumeSliderListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (volumeControl != null) {
                float volume = volumeSlider.getValue() / 100f;
                volumeControl.setValue(volume);
            }
        }
    }

    private void playSong() {
        if (clip != null && clip.isOpen()) {
            clip.close();
        }

        try {
            File songFile = songList.get(currentSongIndex);
            audioInputStream = AudioSystem.getAudioInputStream(songFile);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float volume = volumeSlider.getValue() / 100f;
            volumeControl.setValue(volume);

            clip.start();
            songLabel.setText("Playing: " + songFile.getName());
            albumCoverLabel.setIcon(albumCoverList.get(currentSongIndex));

            progressBar.setMaximum((int) clip.getMicrosecondLength() / 1000);
            progressBar.setValue(0);

            timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (clip != null && clip.isRunning()) {
                        progressBar.setValue((int) clip.getMicrosecondPosition() / 1000);
                    }
                }
            });
            timer.start();

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    timer.stop();
                    progressBar.setValue(progressBar.getMaximum());
                }
            });
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void stopPlayback() {
        if (clip != null) {
            clip.stop();
            clip.close();
            if (timer != null) {
                timer.stop();
            }
            progressBar.setValue(0);
            songLabel.setText("Playback stopped.");
        }
    }

    private void showPlaylist() {
        StringBuilder playlist = new StringBuilder();
        for (int i = 0; i < songList.size(); i++) {
            playlist.append((i + 1)).append(". ").append(songList.get(i).getName()).append("\n");
        }
        JOptionPane.showMessageDialog(frame, playlist.toString(), "Playlist", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MusicPlayer::new);
    }

    private class VisualizationPanel extends JPanel {
        public VisualizationPanel() {
            setPreferredSize(new Dimension(300, 200));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Placeholder for visualization rendering
        }
    }
}
