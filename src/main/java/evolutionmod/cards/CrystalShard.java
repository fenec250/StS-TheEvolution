package evolutionmod.cards;

import basemod.abstracts.CustomCard;
import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.BeastGene;
import evolutionmod.orbs.HarpyGene;
import evolutionmod.orbs.InsectGene;
import evolutionmod.orbs.LavafolkGene;
import evolutionmod.orbs.LizardGene;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.orbs.MerfolkGene;
import evolutionmod.orbs.PlantGene;
import evolutionmod.orbs.ShadowGene;
import evolutionmod.orbs.SuccubusGene;
import evolutionmod.patches.AbstractCardEnum;

public class CrystalShard
		extends BaseEvoCard implements CustomSavable<Integer> {
	public static final String ID = "evolutionmod:CrystalShard";
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";
	private static final int COST = 1;
	private static final int DAMAGE_AMT = 8;
	private static final int UPGRADE_DAMAGE_AMT = 2;
	private static final int FORM_DAMAGE = 4;
	private static final int UPGRADE_FORM_DAMAGE = 2;

	private int genesIndexes;
	private AbstractGene firstGene; // firstIndex = genesIndex / 10
	private AbstractGene secondGene; // secoIndex = genesIndex % 10

	public CrystalShard() {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION + EXTENDED_DESCRIPTION[0],
				CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
				CardRarity.COMMON, CardTarget.ENEMY);
		this.damage = this.baseDamage = DAMAGE_AMT;
		this.magicNumber = this.baseMagicNumber = FORM_DAMAGE;
		this.exhaust = true;
		this.genesIndexes = -1;
		resetGene();
	}

	private CrystalShard(int geneIndexes) {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION + EXTENDED_DESCRIPTION[0],
				CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
				CardRarity.BASIC, CardTarget.SELF);
		this.damage = this.baseDamage= DAMAGE_AMT;
		this.magicNumber = this.baseMagicNumber = FORM_DAMAGE;
		this.exhaust = true;
		this.genesIndexes = geneIndexes;
		resetGene();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int damage = this.damage;

		if (!AbstractGene.isPlayerInThisForm(this.firstGene.ID)) {
			addToBot(new ChannelAction(this.firstGene.makeCopy()));
		} else {
			damage += this.magicNumber;
		}
		addToBot(new DamageAction(
				m, new DamageInfo(p, damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.BLUNT_HEAVY));
		if (!AbstractGene.isPlayerInThisForm(this.secondGene.ID)) {
			addToBot(new ChannelAction(this.secondGene.makeCopy()));
		} else {
			addToBot(new DrawCardAction(p, 1));
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return this.secondGene == null ? new CrystalShard() : new CrystalShard(this.genesIndexes);
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(UPGRADE_DAMAGE_AMT);
			this.upgradeMagicNumber(UPGRADE_FORM_DAMAGE);
		}
	}

	private void resetGene() {
		if (this.genesIndexes < 0) {
			if (!CardCrawlGame.isInARun() || AbstractDungeon.miscRng == null) {
				return;
			}
			this.genesIndexes = AbstractDungeon.miscRng.random(10 * 10 - 1);
		}
		AbstractGene[] validGenes = {
				new PlantGene(),
				new MerfolkGene(),
				new HarpyGene(),
				new LavafolkGene(),
				new SuccubusGene(),
				new LymeanGene(),
				new InsectGene(),
				new BeastGene(),
				new LizardGene(),
				new ShadowGene()};
		this.firstGene =  validGenes[this.genesIndexes / 10];
		this.secondGene = validGenes[this.genesIndexes % 10];
		this.rawDescription = DESCRIPTION
				+ this.firstGene.getColoredName() + EXTENDED_DESCRIPTION[1]
				+ this.secondGene.getColoredName() + EXTENDED_DESCRIPTION[2];
		initializeDescription();
	}

	@Override
	public Integer onSave() {
		return this.genesIndexes;
	}

	@Override
	public void onLoad(Integer integer) {
		this.genesIndexes = integer;
		resetGene();
	}
}