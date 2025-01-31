import pygame
import random
import os

pygame.init()

# Constants
GRID_SIZE = 20
CELL_SIZE = 40
SCREEN_SIZE = GRID_SIZE * CELL_SIZE
FPS = 5

# Image paths and loading
ASSETS_DIR = os.path.join(os.path.dirname(__file__), 'assets')
PLAYER_IMG_PATH = os.path.join(ASSETS_DIR, 'player.png')
GOLD_IMG_PATH = os.path.join(ASSETS_DIR, 'gold.png')
DAMAGE_IMG_PATH = os.path.join(ASSETS_DIR, 'damage.png')
OBSTACLE_IMG_PATH = os.path.join(ASSETS_DIR, 'obstacle.png')

# Load images if available, otherwise None
try:
    PLAYER_IMG = pygame.image.load(PLAYER_IMG_PATH)
    PLAYER_IMG = pygame.transform.scale(PLAYER_IMG, (CELL_SIZE, CELL_SIZE))
except:
    PLAYER_IMG = None

try:
    GOLD_IMG = pygame.image.load(GOLD_IMG_PATH)
    GOLD_IMG = pygame.transform.scale(GOLD_IMG, (CELL_SIZE, CELL_SIZE))
except:
    GOLD_IMG = None

try:
    DAMAGE_IMG = pygame.image.load(DAMAGE_IMG_PATH)
    DAMAGE_IMG = pygame.transform.scale(DAMAGE_IMG, (CELL_SIZE, CELL_SIZE))
except:
    DAMAGE_IMG = None

try:
    OBSTACLE_IMG = pygame.image.load(OBSTACLE_IMG_PATH)
    OBSTACLE_IMG = pygame.transform.scale(OBSTACLE_IMG, (CELL_SIZE, CELL_SIZE))
except:
    OBSTACLE_IMG = None

# Colors (fallback if images not available)
WALKABLE_COLOR = (255, 255, 255)
OBSTACLE_COLOR = (0, 0, 0)
GOLD_COLOR = (255, 223, 0)
DAMAGE_COLOR = (255, 0, 0)
PLAYER_COLOR = (128, 0, 128)
HP_COLOR = (50, 205, 50)
TEXT_COLOR = (255, 255, 255)
PATH_COLOR = (173, 216, 230)

# Cell Types
WALKABLE = 0
OBSTACLE = 1
GOLD = 2
DAMAGE = 3

# Costs and weights
DAMAGE_COST = 8
NORMAL_COST = 1
HEALTH_WEIGHT = 0.5

# Initialize Screen
screen = pygame.display.set_mode((SCREEN_SIZE, SCREEN_SIZE))
pygame.display.set_caption("AI Treasure hunt Game")
clock = pygame.time.Clock()

# Player Stats
player_hp = 100
player_gold = 0
player_position = (0, 0)

def distance(start, end):
    return abs(start[0] - end[0]) + abs(start[1] - end[1])

def A_Star(grid, start, goal, current_hp=100):
    open_set = {start}
    closed_set = set()
    
    came_from = {}
    
    g_score = {start: 0}
    f_score = {start: distance(start, goal)}
    
    while open_set:
        current = min(open_set, key=lambda pos: f_score.get(pos, float('inf')))
        
        if current == goal:
            path = []
            while current in came_from:
                path.append(current)
                current = came_from[current]
            path.append(start)
            return path[::-1]
            
        open_set.remove(current)
        closed_set.add(current)
        
        for dx, dy in [(-1, 0), (1, 0), (0, -1), (0, 1)]:
            neighbor = (current[0] + dx, current[1] + dy)
            
            if not (0 <= neighbor[0] < GRID_SIZE and 0 <= neighbor[1] < GRID_SIZE):
                continue
                
            if grid[neighbor[1]][neighbor[0]] == OBSTACLE:
                continue
                
            if neighbor in closed_set:
                continue
            
            move_cost = DAMAGE_COST if grid[neighbor[1]][neighbor[0]] == DAMAGE else NORMAL_COST
            
            path_damage = sum(DAMAGE_COST if grid[pos[1]][pos[0]] == DAMAGE else 0 
                            for pos in path_to_current(came_from, current, start))
            path_damage += DAMAGE_COST if grid[neighbor[1]][neighbor[0]] == DAMAGE else 0
            
            if current_hp - path_damage <= 10:
                continue
            
            tentative_g_score = g_score.get(current, float('inf')) + move_cost
            
            if neighbor not in open_set:
                open_set.add(neighbor)
            elif tentative_g_score >= g_score.get(neighbor, float('inf')):
                continue
                
            came_from[neighbor] = current
            g_score[neighbor] = tentative_g_score
            f_score[neighbor] = g_score[neighbor] + distance(neighbor, goal)
    
    return []

def path_to_current(came_from, current, start):
    path = []
    while current in came_from:
        path.append(current)
        current = came_from[current]
    path.append(start)
    return path[::-1]

def verify_path_exists(grid, start, end):
    path = A_Star(grid, start, end)
    return len(path) > 0

def create_grid():
  
    while True:
        grid = [[WALKABLE for _ in range(GRID_SIZE)] for _ in range(GRID_SIZE)]
        grid[0][0] = WALKABLE

        """      grid = []
              for _ in range(GRID_SIZE):
             row = []
             for _ in range(GRID_SIZE):
                        row.append(WALKABLE)
                  grid.append(row)"""

        
        # Place obstacles (12%)
        obstacle_count = int(GRID_SIZE * GRID_SIZE * 0.12)
        for _ in range(obstacle_count):
            x, y = random.randint(0, GRID_SIZE - 1), random.randint(0, GRID_SIZE - 1)
            if (x, y) != (0, 0):
                grid[y][x] = OBSTACLE
        
        # Place damage cells (15%)
        damage_count = int(GRID_SIZE * GRID_SIZE * 0.15)
        for _ in range(damage_count):
            x, y = random.randint(0, GRID_SIZE - 1), random.randint(0, GRID_SIZE - 1)
            if (x, y) != (0, 0) and grid[y][x] == WALKABLE:
                grid[y][x] = DAMAGE
        
        # Find valid positions for gold
        valid_positions = []
        for y in range(GRID_SIZE):
            for x in range(GRID_SIZE):
                if grid[y][x] == WALKABLE and (x, y) != (0, 0):
                    if verify_path_exists(grid, (0, 0), (x, y)):
                        valid_positions.append((x, y))
        
        # Ensure exactly 10 gold pieces can be placed
        if len(valid_positions) >= 10:
            gold_positions = random.sample(valid_positions, 10)
            
            # Verify all gold positions are reachable
            all_reachable = True
            for gold_pos in gold_positions:
                if not verify_path_exists(grid, (0, 0), gold_pos):
                    all_reachable = False
                    break
            
            if all_reachable:
                for x, y in gold_positions:
                    grid[y][x] = GOLD
                return grid, gold_positions
        
        # If we couldn't create a valid grid, try again

def find_nearest_gold(grid, current_pos, gold_positions, current_hp):
    if not gold_positions:
        return None, []
        
    best_path = None
    best_gold = None
    min_cost = float('inf')
    
    for gold_pos in gold_positions:
        path = A_Star(grid, current_pos, gold_pos, current_hp)
        if path:
            path_cost = len(path)
            damage_cost = sum(DAMAGE_COST if grid[y][x] == DAMAGE else 0 
                            for x, y in path)
            total_cost = path_cost + (damage_cost * HEALTH_WEIGHT)
            
            if total_cost < min_cost:
                min_cost = total_cost
                best_path = path
                best_gold = gold_pos
    
    return best_gold, best_path

def draw_stats(gold_positions):
    stats_surface = pygame.Surface((SCREEN_SIZE, 60))
    stats_surface.set_alpha(180)
    stats_surface.fill((0, 0, 0))
    screen.blit(stats_surface, (0, 0))
    
    font = pygame.font.Font(None, 36)
    
    hp_text = font.render(f'HP: {player_hp}', True, HP_COLOR)
    screen.blit(hp_text, (10, 10))
    
    gold_text = font.render(f'Gold: {player_gold}', True, GOLD_COLOR)
    screen.blit(gold_text, (150, 10))
    
    remaining_text = font.render(f'Remaining Gold: {len(gold_positions)}', True, GOLD_COLOR)
    screen.blit(remaining_text, (300, 10))

def draw_grid(grid, gold_positions, current_path=None):
    screen.fill((150, 150, 150))
    
    for y in range(GRID_SIZE):
        for x in range(GRID_SIZE):
            rect = pygame.Rect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE)
            
            if grid[y][x] == OBSTACLE:
                if OBSTACLE_IMG:
                    screen.blit(OBSTACLE_IMG, rect)
                else:
                    pygame.draw.rect(screen, OBSTACLE_COLOR, rect)
            elif grid[y][x] == GOLD:
                if GOLD_IMG:
                    screen.blit(GOLD_IMG, rect)
                else:
                    pygame.draw.rect(screen, GOLD_COLOR, rect)
            elif grid[y][x] == DAMAGE:
                if DAMAGE_IMG:
                    screen.blit(DAMAGE_IMG, rect)
                else:
                    pygame.draw.rect(screen, DAMAGE_COLOR, rect)
            else:
                pygame.draw.rect(screen, WALKABLE_COLOR, rect)
            
            # Draw grid lines
            pygame.draw.rect(screen, (100, 100, 100), rect, 1)
    
    if current_path:
        for index, pos in enumerate(current_path[1:]):
            x, y = pos
            # Check if this is the last position in the path
            if (x, y) != player_position and index != len(current_path) - 2:  # Adjusted index for the last position
                pygame.draw.rect(screen, PATH_COLOR, 
                               (x * CELL_SIZE + 5, y * CELL_SIZE + 5, 
                                CELL_SIZE - 10, CELL_SIZE - 10))
    
    # Draw player
    px, py = player_position
    player_rect = pygame.Rect(px * CELL_SIZE, py * CELL_SIZE, CELL_SIZE, CELL_SIZE)
    if PLAYER_IMG:
        screen.blit(PLAYER_IMG, player_rect)
    else:
        pygame.draw.rect(screen, PLAYER_COLOR, player_rect)
    
    draw_stats(gold_positions)

def draw_game_over():
    overlay = pygame.Surface((SCREEN_SIZE, SCREEN_SIZE))
    overlay.set_alpha(180)
    overlay.fill((0, 0, 0))
    screen.blit(overlay, (0, 0))
    
    font = pygame.font.Font(None, 74)
    if player_hp <= 0:
        text = font.render('Game Over!', True, DAMAGE_COLOR)
    else:
        text = font.render('Winner!', True, GOLD_COLOR)
    
    text_rect = text.get_rect(center=(SCREEN_SIZE/2, SCREEN_SIZE/2))
    screen.blit(text, text_rect)
    
    stats_font = pygame.font.Font(None, 48)
    stats_text = stats_font.render(f'Final Gold: {player_gold}', True, GOLD_COLOR)
    stats_rect = stats_text.get_rect(center=(SCREEN_SIZE/2, SCREEN_SIZE/2 + 50))
    screen.blit(stats_text, stats_rect)

def main():
    global player_position, player_gold, player_hp
    
    
    grid, gold_positions = create_grid()
    gold_positions = set(gold_positions)
    
    running = True
    game_won = False
    current_path = None
    
    while running:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False
            elif event.type == pygame.KEYDOWN:
                if event.key == pygame.K_ESCAPE:
                    running = False
        
        if not game_won and gold_positions and player_hp > 0:
            if not current_path or len(current_path) <= 1:
                nearest_gold, current_path = find_nearest_gold(grid, player_position, gold_positions, player_hp)
            
            if current_path and len(current_path) > 1:
                player_position = current_path[1]
                current_path = current_path[1:]
                
                px, py = player_position
                
                if grid[py][px] == DAMAGE:
                    damage = random.randint(10, 20)
                    player_hp -= damage
                
                if grid[py][px] == GOLD:
                    player_gold += 10
                    grid[py][px] = WALKABLE
                    gold_positions.remove((px, py))
                    current_path = None
                    
                    if not gold_positions:
                        game_won = True
            else:
                nearest_gold, current_path = find_nearest_gold(grid, player_position, gold_positions, player_hp)
                if not current_path:
                    print("No viable path to remaining gold.")
                    running = False
        
        draw_grid(grid, gold_positions, current_path)
        
        if game_won or player_hp <= 0:
            draw_game_over()
            pygame.display.flip()
            pygame.time.wait(2000)
            running = False
        else:
            pygame.display.flip()
        
        clock.tick(FPS)
    
    pygame.quit()

if __name__ == "__main__":
    main()
