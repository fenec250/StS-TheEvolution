package evolutionmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import evolutionmod.orbs.BeastGene;
import evolutionmod.orbs.GhostGene;
import evolutionmod.patches.AbstractCardEnum;

public class BlackCat
        extends CustomCard {
    public static final String ID = "evolutionmod:BlackCat";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/BlackCat.png";
    private static final int COST = 1;
    private static final int DAMAGE_AMT = 4;
    private static final int UPGRADE_DAMAGE_AMT = 2;
    private static final int WEAK_AMT = 1;
    private static final int UPGRADE_WEAK_AMT = 1;

    public BlackCat() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = WEAK_AMT;
        this.isMultiDamage = true;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, this.multiDamage,
                this.damageTypeForTurn,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, this.multiDamage,
                this.damageTypeForTurn,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));

        AbstractDungeon.getMonsters().monsters.stream()
                .filter(mo -> !mo.isDeadOrEscaped())
                .forEach(mo -> addToBot(new ApplyPowerAction(mo, p,
                        new WeakPower(mo, this.magicNumber, false), this.magicNumber)));

        AbstractDungeon.actionManager.addToBottom(new ChannelAction(new BeastGene()));
        AbstractDungeon.actionManager.addToBottom(new ChannelAction(new GhostGene()));
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
            this.upgradeMagicNumber(UPGRADE_WEAK_AMT);
        }
    }
}
