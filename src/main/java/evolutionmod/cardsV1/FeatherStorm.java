package evolutionmod.cardsV1;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.cards.BaseEvoCard;
import evolutionmod.cards.Feather;
import evolutionmod.orbsV1.HarpyGene;
import evolutionmod.patches.EvolutionEnum;

public class FeatherStorm
        extends BaseEvoCard {
    public static final String cardID = "FeatherStorm";
    public static final String ID = "evolutionmod:"+cardID;
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("evolutionmod:"+cardID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/FeatherStorm.png";
    private static final int COST = 0;
    private static final int DAMAGE = 6;
    private static final int UPGRADE_DAMAGE = 2;
    private static final int FEATHER_AMT = 2;
    private static final int UPGRADE_FEATHER_AMT = 1;

    private boolean shouldSpawnFeathers;

    public FeatherStorm() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, EvolutionEnum.EVOLUTION_BLUE,
                CardRarity.RARE, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE;
        this.magicNumber = this.baseMagicNumber = FEATHER_AMT;
        this.shouldSpawnFeathers = true;
        this.cardsToPreview = new Feather(HarpyGene.ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(
                m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        if (this.shouldSpawnFeathers) {
            addToBot(new MakeTempCardInDrawPileAction(new Feather(HarpyGene.ID), this.magicNumber, true, true));
            this.shouldSpawnFeathers = false;
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new FeatherStorm();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        FeatherStorm copy = (FeatherStorm) super.makeStatEquivalentCopy();
        copy.shouldSpawnFeathers = this.shouldSpawnFeathers;
        return copy;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE);
            this.upgradeMagicNumber(UPGRADE_FEATHER_AMT);
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (this.shouldSpawnFeathers) {
            this.glowColor = GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}