package evolutionmod.cards;

import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.BeastGene;
import evolutionmod.orbs.ShadowGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.ShadowsPower;

import java.util.List;

public class BlackCat
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:BlackCat";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/BlackCat.png";
    private static final int COST = 1;
    private static final int DAMAGE_AMT = 4;
    private static final int UPGRADE_DAMAGE_AMT = 2;
    private static final int WEAK_AMT = 1;

    public BlackCat() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = WEAK_AMT;
        this.isMultiDamage = true;
//        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, this.multiDamage,
                this.damageTypeForTurn,
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        BaseEvoCard.formEffect(BeastGene.ID, () -> AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, this.multiDamage,
                    this.damageTypeForTurn,
                    AbstractGameAction.AttackEffect.SLASH_DIAGONAL)));

        BaseEvoCard.formEffect(ShadowGene.ID, () -> addToBot(new AbstractGameAction() {
            @Override
            public void update() {
//                ShadowsPower.reduceThreshold(p, magicNumber);
                ShadowsPower.triggerImmediately(p);
                this.isDone = true;
            }
        }));
//        BaseEvoCard.formEffect(ShadowGene.ID, () -> AbstractDungeon.getMonsters().monsters.stream()
//                    .filter(mo -> !mo.isDeadOrEscaped())
//                    .forEach(mo -> addToBot(new ApplyPowerAction(mo, p,
//                            new WeakPower(mo, this.magicNumber, false), this.magicNumber))));
    }

    @Override
    public AbstractCard makeCopy() {
        return new BlackCat();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isPlayerInTheseForms(BeastGene.ID, ShadowGene.ID)) {
            this.glowColor = GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        if (customTooltips == null) {
            super.getCustomTooltips();
            customTooltips.add(new TooltipInfo(EXTENDED_DESCRIPTION[0],
                    EXTENDED_DESCRIPTION[1]));
        }
        return  customTooltips;
    }
}
