# IfThenLearning
A machine learning algorithm that achieves optimal play for any game given only the rules

This project is a simulation of a population of agents that can learn to play (theoretically) any game through competition. The
software is designed so that defining a game can be done within a single class. Currently, the only game implemented is Hexapawn. It is
a very simple and very small game.

The agents (defined abstractly in Player.java and concretely in Phenotype.java) contain hash maps with game states (defined in
State.java) as keys, and moves (defined in Move.java) as values. When an agent is asked for a move, it is given the current game state.
It then plugs that game state into its hash map. If the hash map contains this key, then the corresponding move is handed back to the
game object (defined abstractly in Game.java and concretely in Hexapawn.java). If it has not encountered this game state before, then a
random move is chosen, and if the agent wins the current game, then this random move and its game state are entered into the hash map.

The population of agents (defined in Population.java) learns via a genetic algorithm, where the phenotypes are the agents, the
genotypes are the key-value pairs in the agents' hash maps, and the fitness score is the win rates of the individual agents. Each
iteration of the population, the agents are sorted with a merge sort algorithm where each comparison is a game (to be honest, that was
the plan, but it's really anyone's guess whether or not it actually fits the definition of merge sort. There were so many bugs in the
process of making that part of the software). After the population is sorted, the bottom half is deleted and replaced with children of
the surviving agents.

There are a number of uses of randomness in this program. First, every phenotype has a chance of ignoring its hash map and giving a
random move. This randomness is defined at instantiation according to the following code:

	if (Math.random() < randomRandomnessRate)
		randomness = Math.random();
	else if (Math.random() < mother.getWinRate() /(mother.getWinRate() + father.getWinRate()))
		randomness = mother.randomness + ((Math.random() * mother.randomness * 0.05) - (mother.randomness * 0.025));
	else
		randomness = father.randomness + ((Math.random() * father.randomness * 0.05) - (father.randomness * 0.025));
randomRandomnessRate is a static constant set to 5% that acts as the limit of the randomness attributes of phenotype objects that don't
have parents. It also acts as the chance that a ne phenotype will not inherit its randomness attribute from its parents. When a child
does inherit its randomness attribute, it is set to that of one parent plus 5% of the other parent's randomness. This way, both parents
have influence, but one parent (most likely the one with the higher win rate) will have significantly more influence.

When it comes to passing on genotypes, each parent's chance of passing a key-value pair to their child is defined by that parent's win
rate in comparison to the other's. For each key-value pair, if a new random number is less than the one parent's win rate divided by the
sum of the win rates of both parents, then that pair is inherited from that parent. Otherwise, it is inherited from the other parent.

When running this program as is, you will be prompted for a population size and generation count. The Population object will be
instantiated to what ever size you give and iterated however many times you specified. Then you will play against the best member of the
population.
