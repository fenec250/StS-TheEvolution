package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.orbs.*;
import evolutionmod.patches.EvolutionEnum;

import java.util.Iterator;

public class Transform extends BaseEvoCard {
	public static final String ID = "evolutionmodV2:Transform";
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/ShiftingPower.png";
	private static final int COST = 1;
	private static final int UPGRADED_COST = 0;

	public Transform() {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
				CardType.SKILL, EvolutionEnum.EVOLUTION_V2_BLUE,
				CardRarity.UNCOMMON, CardTarget.SELF);
		this.exhaust = true;
		this.cardsToPreview = new Mutate();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		Iterator<AbstractOrb> genes = p.orbs.stream()
				.filter(orb -> orb instanceof AbstractGene)
				.limit(2)
				.iterator();
		String gene1 = "", gene2 = "";
		if (genes.hasNext())
			gene1 = genes.next().ID;
		if (genes.hasNext())
			gene2 = genes.next().ID;
		AbstractCard c = getCard(gene1, gene2);
		c.setCostForTurn(0);
		addToBot(new MakeTempCardInHandAction(c));
	}

	@Override
	public AbstractCard makeCopy() {
		return new Transform();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeBaseCost(UPGRADED_COST);
		}
	}

//	private float previewTimer = 0f;
//	private int previewIndex = 0;
//	private static final List<AbstractCard> previewCards = new ArrayList<AbstractCard>() {{
//			add(getCard(PlantGene2.ID,HarpyGene2.ID));
//			add(getCard(PlantGene2.ID,LavafolkGene2.ID));
//			add(getCard(PlantGene2.ID,SuccubusGene2.ID));
//			add(getCard(PlantGene2.ID,BeastGene2.ID));
//			add(getCard(PlantGene2.ID,null));
//		}};
//	@Override
//	public void update() {
//		super.update();
//		if (hb.justHovered) {
//			this.previewTimer = -1f;
//		}
//		if (hb.hovered) {
//			this.previewTimer -= Gdx.graphics.getDeltaTime();
//			if (previewTimer < 0) {
//				previewIndex = (previewIndex + 1) % 3;
//				this.cardsToPreview = previewCards.get(previewIndex);
//				previewTimer = 3f;
//			}
//		}
//	}
	@Override
	public void update() {
		super.update();
		if (hb.justHovered && CardCrawlGame.isInARun()) {
			Iterator<AbstractOrb> genes = AbstractDungeon.player.orbs.stream()
					.filter(orb -> orb instanceof AbstractGene)
					.limit(2)
					.iterator();
			String gene1 = "", gene2 = "";
			if (genes.hasNext())
				gene1 = genes.next().ID;
			if (genes.hasNext())
				gene2 = genes.next().ID;
			this.cardsToPreview = getCard(gene1, gene2);
		}
	}

	private static AbstractCard getCard(String gene1, String gene2) {
		switch (gene1) {
			case PlantGene2.ID: switch (gene2) {
//				case PlantGene2.ID: return new LeafBird2();
//				case MerfolkGene2.ID: return new CurrentsDancer2();
				case HarpyGene2.ID: return new LeafWings();
				case LavafolkGene2.ID: return new Blazebloom();
				case SuccubusGene2.ID: return new Pheromones2();
//				case LymeanGene2.ID: return new ReadTheWaters2();
//				case InsectGene2.ID: return new CoralStewards();
				case BeastGene2.ID: return new Mossbeast();
//				case LizardGene2.ID: return new Toxin2();
//				case CentaurGene2.ID: return new PegasusDescent();
//				case ShadowGene2.ID: return new DepthsLurker6();
				default: return new Sapling();
			}
			case MerfolkGene2.ID: switch (gene2) {
//				case PlantGene2.ID: return new LeafBird2();
//				case MerfolkGene2.ID: return new CurrentsDancer2();
				case HarpyGene2.ID: return new CurrentsDancer();
//				case LavafolkGene2.ID: return new Blazebloom();
//				case SuccubusGene2.ID: return new Pheromones2();
				case LymeanGene2.ID: return new ReadTheWaters2();
				case InsectGene2.ID: return new CoralStewards();
				case BeastGene2.ID: return new SeaWolf2();
				case LizardGene2.ID: return new SeaSerpent();
//				case CentaurGene2.ID: return new PegasusDescent();
				case ShadowGene2.ID: return new DepthsLurker();
				default: return new Dive();
			}
			case HarpyGene2.ID: switch (gene2) {
				case PlantGene2.ID: return new LeafWings();
				case MerfolkGene2.ID: return new CurrentsDancer();
//				case HarpyGene2.ID: return new CurrentsDancer();
				case LavafolkGene2.ID: return new Phoenix();
//				case SuccubusGene2.ID: return new Pheromones2();
//				case LymeanGene2.ID: return new ReadTheWaters2();
//				case InsectGene2.ID: return new CoralStewards();
				case BeastGene2.ID: return new HeightenedSenses();
//				case LizardGene2.ID: return new SeaSerpent();
				case CentaurGene2.ID: return new PegasusDescent();
//				case ShadowGene2.ID: return new DepthsLurker6();
				default: return new TalonStrike();
			}
			case LavafolkGene2.ID: switch (gene2) {
				case PlantGene2.ID: return new Blazebloom();
//				case MerfolkGene2.ID: return new CurrentsDancer();
				case HarpyGene2.ID: return new Phoenix();
//				case LavafolkGene2.ID: return new Phoenix();
//				case SuccubusGene2.ID: return new Pheromones2();
				case LymeanGene2.ID: return new SeerSear();
				case InsectGene2.ID: return new FireAnts();
				case BeastGene2.ID: return new Embermane();
				case LizardGene2.ID: return new Salamander();
//				case CentaurGene2.ID: return new PegasusDescent();
//				case ShadowGene2.ID: return new DepthsLurker6();
				default: return new FlameStrike();
			}
			case SuccubusGene2.ID: switch (gene2) {
				case PlantGene2.ID: return new Pheromones2();
//				case MerfolkGene2.ID: return new CurrentsDancer();
//				case HarpyGene2.ID: return new Phoenix();
//				case LavafolkGene2.ID: return new Phoenix();
//				case SuccubusGene2.ID: return new Pheromones2();
				case LymeanGene2.ID: return new Promise();
//				case InsectGene2.ID: return new FireAnts();
				case BeastGene2.ID: return new StripArmor2();
				case LizardGene2.ID: return new Heartstopper();
				case CentaurGene2.ID: return new PlayingRough();
				case ShadowGene2.ID: return new CursedTouch2();
				default: return new Charm();
			}
			case LymeanGene2.ID: switch (gene2) {
//				case PlantGene2.ID: return new Pheromones2();
				case MerfolkGene2.ID: return new ReadTheWaters2();
//				case HarpyGene2.ID: return new Phoenix();
				case LavafolkGene2.ID: return new SeerSear();
				case SuccubusGene2.ID: return new Promise();
//				case LymeanGene2.ID: return new Promise();
				case InsectGene2.ID: return new Hivemind();
//				case BeastGene2.ID: return new StripArmor2();
//				case LizardGene2.ID: return new Heartstopper();
				case CentaurGene2.ID: return new TrueStrike();
//				case ShadowGene2.ID: return new DepthsLurker6();
				default: return new Visions();
			}
			case InsectGene2.ID: switch (gene2) {
//				case PlantGene2.ID: return new Pheromones2();
				case MerfolkGene2.ID: return new CoralStewards();
//				case HarpyGene2.ID: return new Phoenix();
				case LavafolkGene2.ID: return new FireAnts();
//				case SuccubusGene2.ID: return new Promise();
				case LymeanGene2.ID: return new Hivemind();
//				case InsectGene2.ID: return new Hivemind2();
//				case BeastGene2.ID: return new StripArmor2();
				case LizardGene2.ID: return new VenomGlands2();
				case CentaurGene2.ID: return new Battleborn();
//				case ShadowGene2.ID: return new DepthsLurker6();
				default: return new Hatch();
			}
			case BeastGene2.ID: switch (gene2) {
				case PlantGene2.ID: return new Mossbeast();
				case MerfolkGene2.ID: return new SeaWolf2();
				case HarpyGene2.ID: return new HeightenedSenses();
				case LavafolkGene2.ID: return new Embermane();
				case SuccubusGene2.ID: return new StripArmor2();
//				case LymeanGene2.ID: return new Promise();
//				case InsectGene2.ID: return new FireAnts();
//				case BeastGene2.ID: return new StripArmor2();
//				case LizardGene2.ID: return new Heartstopper();
//				case CentaurGene2.ID: return new PlayingRough();
				case ShadowGene2.ID: return new BlackCat4();
				default: return new ClawSlash();
			}
			case LizardGene2.ID: switch (gene2) {
				case PlantGene2.ID: return new Toxin2();
				case MerfolkGene2.ID: return new SeaSerpent();
//				case HarpyGene2.ID: return new Phoenix();
				case LavafolkGene2.ID: return new Salamander();
				case SuccubusGene2.ID: return new Heartstopper();
//				case LymeanGene2.ID: return new Hivemind2();
				case InsectGene2.ID: return new VenomGlands2();
//				case BeastGene2.ID: return new StripArmor2();
//				case LizardGene2.ID: return new VenomGlands2();
//				case CentaurGene2.ID: return new Battleborn();
				case ShadowGene2.ID: return new DeathKiss();
				default: return new PoisonScales();
			}
			case CentaurGene2.ID: switch (gene2) {
//				case PlantGene2.ID: return new Pheromones2();
//				case MerfolkGene2.ID: return new CoralStewards();
				case HarpyGene2.ID: return new PegasusDescent();
//				case LavafolkGene2.ID: return new FireAnts();
				case SuccubusGene2.ID: return new PlayingRough();
				case LymeanGene2.ID: return new TrueStrike();
				case InsectGene2.ID: return new Battleborn();
//				case BeastGene2.ID: return new StripArmor2();
//				case LizardGene2.ID: return new VenomGlands2();
//				case CentaurGene2.ID: return new Battleborn();
				case ShadowGene2.ID: return new NightMare();
				default: return new HeavyKick();
			}
			case ShadowGene2.ID: switch (gene2) {
//				case PlantGene2.ID: return new Pheromones2();
				case MerfolkGene2.ID: return new DepthsLurker();
//				case HarpyGene2.ID: return new PegasusDescent();
//				case LavafolkGene2.ID: return new FireAnts();
				case SuccubusGene2.ID: return new CursedTouch2();
//				case LymeanGene2.ID: return new TrueStrike();
//				case InsectGene2.ID: return new Battleborn();
				case BeastGene2.ID: return new BlackCat4();
				case LizardGene2.ID: return new DeathKiss();
				case CentaurGene2.ID: return new NightMare();
//				case ShadowGene2.ID: return new NightMare4();
				default: return new ShadowBolt();
			}
			default:
				return new Mutate();
		}
	}
}